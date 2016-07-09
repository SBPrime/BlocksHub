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

package org.primesoft.blockshub.logger.bukkit.prism;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionFactory;
import me.botsko.prism.actions.Handler;
import me.botsko.prism.actionlibs.RecordingQueue;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.primesoft.blockshub.api.BlockData;
import org.primesoft.blockshub.api.IBlockLogger;
import org.primesoft.blockshub.api.ILog;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.IWorld;
import org.primesoft.blockshub.api.Vector;
import org.primesoft.blockshub.platform.bukkit.BukkitBaseEntity;
import org.primesoft.blockshub.platform.bukkit.BukkitPlayer;
import org.primesoft.blockshub.platform.bukkit.BukkitWorld;
import org.primesoft.blockshub.utils.ExceptionHelper;

/**
 *
 * @author SBPrime
 */
public class PrismLogger extends BukkitBaseEntity implements IBlockLogger {
    /**
     * All prism hooks
     */
    private static final PrismLoggerHook[] s_hooks = new PrismLoggerHook[]{
        new PrismLoggerHook(ActionFactory.class, "2.x", "createBlockChange", new Class<?>[]{
            String.class,
            Location.class, int.class, byte.class, int.class, byte.class,
            String.class}),
        new PrismLoggerHook(ActionFactory.class, "1.x", "create", new Class<?>[]{String.class,
            Location.class, int.class, byte.class, int.class, byte.class,
            String.class})
    };
    
    
    static IBlockLogger create(ILog logger, Object plugin) {
        if (!(plugin instanceof Prism)) {
            logger.log("plugin not found.");
            return null;
        }
        
        Method m = getMethod(logger);
        
        return new PrismLogger((Prism)plugin, m);
    }
    
    
    private static Method getMethod(ILog logger) {
        try {
            for (PrismLoggerHook hook : s_hooks) {
                Method m = hook.getMethod();

                if (m != null) {
                    logger.log(String.format("Detected Prism API {0}", hook.getVersion()));
                    return m;
                }
            }

            logger.log("Unable to detect prism API version. Disabling");
            return null;
        }
        catch (SecurityException ex) {
            logger.log("Unable to detect prism API version (Security exception). Disabling");
            
            
            return null;
        }
    }
    

    /**
     * The log method
     */
    private final Method m_logMethod;

    private PrismLogger(Prism prism, Method method) {
        super(prism);
        
        m_logMethod = method;
    }

    @Override
    public void logBlock(Vector location, IPlayer player, IWorld world, BlockData oldBlock, BlockData newBlock) {
        if (!isEnabled()) {
            return;
        }

        if (!(world instanceof BukkitWorld)) {
            return;
        }

        Location l = new Location(((BukkitWorld) world).getWorld(), location.getX(), location.getY(), location.getZ());

        BukkitPlayer bukkitPlayer = BukkitPlayer.getPlayer(player);
        Player bPlayer = bukkitPlayer != null ? bukkitPlayer.getPlayer() : null;
        if (bPlayer == null) {
            return;
        }
        
        
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
                "world-edit", l, oldBlock.getType(), (byte)oldBlock.getData(), 
                                 newBlock.getType(), (byte)newBlock.getData(),
                                 bPlayer});
            RecordingQueue.addToQueue(action);
        }
        catch (IllegalAccessException ex) {
            ExceptionHelper.printException(ex, "PrismLogger");            
        }
        catch (IllegalArgumentException ex) {
            ExceptionHelper.printException(ex, "PrismLogger");
        }
        catch (InvocationTargetException ex) {
            ExceptionHelper.printException(ex, "PrismLogger");
        }
    }
}
