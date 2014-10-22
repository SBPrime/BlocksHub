/*
 * BlocksHub a library plugin providing easy access to block loggers 
 * and block access controllers.
 * Copyright (c) 2013, SBPrime <https://github.com/SBPrime/>
 * Copyright (c) BlocksHub contributors
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted free of charge provided that the following 
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution,
 * 3. Redistributions of source code, with or without modification, in any form 
 *    other then free of charge is not allowed,
 * 4. Redistributions in binary form in any form other then free of charge is 
 *    not allowed.
 * 5. Any derived work based on or containing parts of this software must reproduce 
 *    the above copyright notice, this list of conditions and the following 
 *    disclaimer in the documentation and/or other materials provided with the 
 *    derived work.
 * 6. The original author of the software is allowed to change the license 
 *    terms or the entire license of the software as he sees fit.
 * 7. The original author of the software is allowed to sublicense the software 
 *    or its parts using any license terms he sees fit.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.PrimeSoft.blocksHub.blocklogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionFactory;
import me.botsko.prism.actions.Handler;
import me.botsko.prism.actionlibs.RecordingQueue;
import org.PrimeSoft.blocksHub.BlocksHub;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author SBPrime
 */
public class PrismLogger implements IBlockLogger {
    /**
     * All prism hooks
     */
    private final PrismLoggerHook[] s_hooks = new PrismLoggerHook[]{
        new PrismLoggerHook(ActionFactory.class, "2.x", "createBlockChange", new Class<?>[]{
            String.class,
            Location.class, int.class, byte.class, int.class, byte.class,
            String.class}),
        new PrismLoggerHook(ActionFactory.class, "1.x", "create", new Class<?>[]{String.class,
            Location.class, int.class, byte.class, int.class, byte.class,
            String.class})
    };

    /**
     * Plugin name
     */
    private final String m_name;

    /**
     * The log method
     */
    private Method m_logMethod;

    /**
     * Is the logger enabled
     */
    private boolean m_isEnabled;

    /**
     * Get instance of the prism plugin
     *
     * @param plugin
     * @return
     */
    public static Prism getPrism(JavaPlugin plugin) {
        try {
            Plugin cPlugin = plugin.getServer().getPluginManager().getPlugin("Prism");

            if ((cPlugin == null) || (!(cPlugin instanceof Prism))) {
                return null;
            }

            return (Prism) cPlugin;
        }
        catch (NoClassDefFoundError ex) {
            return null;
        }
    }

    public PrismLogger(JavaPlugin plugin) {
        Prism prism = getPrism(plugin);
        PluginDescriptionFile pd = null;
        if (prism == null) {
            m_isEnabled = false;
        } else {
            pd = prism.getDescription();
            if (pd != null) {
                m_isEnabled = getMethod();
            } else {
                m_isEnabled = false;
            }
        }

        m_name = pd != null ? pd.getFullName() : "Disabled - PrismLogger";
    }

    @Override
    public void logBlock(Location location, String player, World world,
                         int oldBlockType, byte oldBlockData, int newBlockType,
                         byte newBlockData) {
        if (!m_isEnabled) {
            return;
        }

        Location l = new Location(world, location.getBlockX(), location.getBlockY(), location.getBlockZ());
        /*
         * Prism version 1.x
         * Handler action = ActionFactory.create("world-edit", l,
         *               oldBlockType, oldBlockData,
         *               newBlockType, newBlockData,
         *               player);
         *
         * Prism.actionsRecorder.addToQueue(action);
         */
        //RecordingQueue.addToQueue(ActionFactory.createBlockChange("world-edit", l, oldBlockType, oldBlockData, newBlockType, newBlockData, player));
        Handler action;
        try {
            action = (Handler) m_logMethod.invoke(null, new Object[]{
                "world-edit", l, oldBlockType, oldBlockData, newBlockType,
                newBlockData, player});
            RecordingQueue.addToQueue(action);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(PrismLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalArgumentException ex) {
            Logger.getLogger(PrismLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvocationTargetException ex) {
            Logger.getLogger(PrismLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public boolean isEnabled() {
        return m_isEnabled;
    }

    @Override
    public String getName() {
        return m_name;
    }

    private boolean getMethod() {
        try {
            for (PrismLoggerHook hook : s_hooks) {
                Method m = hook.getMethod();

                if (m != null) {
                    BlocksHub.log("Detected Prism API " + hook.getVersion());
                    m_logMethod = m;
                    return true;
                }
            }

            BlocksHub.log("Unable to detect prism API version. Disabling");
            m_logMethod = null;
            return false;
        }
        catch (SecurityException ex) {
            BlocksHub.log("Unable to detect prism API version (Security exception). Disabling");
            m_logMethod = null;
            return false;
        }
    }
}
