/*
 * The MIT License
 *
 * Copyright 2013 SBPrime.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
