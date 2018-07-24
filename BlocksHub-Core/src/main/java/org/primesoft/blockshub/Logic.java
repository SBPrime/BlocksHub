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
package org.primesoft.blockshub;

import org.primesoft.blockshub.api.IBlockLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.primesoft.blockshub.api.BlockData;
import org.primesoft.blockshub.api.IAccessController;
import org.primesoft.blockshub.api.Vector;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.IWorld;
import static org.primesoft.blockshub.api.base.LoggerProvider.log;
import org.primesoft.blockshub.configuration.ConfigProvider;
import org.primesoft.blockshub.inner.api.platform.Colors;
import org.primesoft.blockshub.inner.api.platform.IEnableAware;
import org.primesoft.blockshub.inner.api.platform.IPlatform;
import org.primesoft.blockshub.platform.LazyPlayer;
import org.primesoft.blockshub.utils.ExceptionHelper;

/**
 *
 * @author SBPrime
 */
public class Logic implements IBlocksHubApi, IEnableAware {
    private final IPlatform m_platform;
    private final Object m_mtaMutex = new Object();
    private final List<IBlockLogger> m_loggers = new ArrayList<>();
    private final List<IAccessController> m_ac = new ArrayList<>();
    private boolean m_isInitialized;    
    
    public Logic(IPlatform platform) {
        m_platform = platform;
    }
    
    @Override
    public double getVersion() {
        return 2.0;
    }

    /**
     * Is the api initialized
     *
     * @return
     */
    @Override
    public boolean isInitialized() {
        return m_isInitialized;
    }

    /**
     * Register blocks logger class
     *
     * @param blocksLogger
     * @return
     */
    @Override
    public boolean registerBlocksLogger(IBlockLogger blocksLogger) {
        synchronized (m_mtaMutex) {
            if (blocksLogger == null || m_loggers.contains(blocksLogger)) {
                return false;
            }
            
            m_loggers.add(blocksLogger);
            log(String.format("Registered %1$s logger.", blocksLogger.getName()));
            return true;
        }
    }

    /**
     * Register blocks access controller
     *
     * @param accessController
     * @return
     */
    @Override
    public boolean registerAccessController(IAccessController accessController) {
        synchronized (m_mtaMutex) {
            if (accessController == null || m_ac.contains(accessController)) {
                return false;
            }
            
            m_ac.add(accessController);
            log(String.format("Registered %1$s access controller.", accessController.getName()));
            return true;
        }
    }

    /**
     * Remove blocks logger class
     *
     * @param blocksLogger
     * @return
     */
    @Override
    public boolean removeBlocksLogger(IBlockLogger blocksLogger) {
        synchronized (m_mtaMutex) {
            if (blocksLogger == null || !m_loggers.contains(blocksLogger)) {
                return false;
            }
            
            m_loggers.remove(blocksLogger);
            log(String.format("Removes %1$s logger.", blocksLogger.getName()));
            return true;
        }
    }

    /**
     * Remove blocks access controller
     *
     * @param accessController
     * @return
     */
    @Override
    public boolean removeAccessController(IAccessController accessController) {
        synchronized (m_mtaMutex) {
            if (accessController == null || !m_ac.contains(accessController)) {
                return false;
            }
            
            m_ac.remove(accessController);
            log(String.format("Removes %1$s access controller.", accessController.getName()));
            return true;
        }
    }

    /**
     * List all registered loggers
     *
     * @return
     */
    @Override
    public IBlockLogger[] getRegisteredLoggers() {
        synchronized (m_mtaMutex) {
            return m_loggers.toArray(new IBlockLogger[0]);
        }
    }

    /**
     * List all registered access controllers
     *
     * @return
     */
    @Override
    public IAccessController[] getRegisteredAccessControllers() {
        synchronized (m_mtaMutex) {
            return m_ac.toArray(new IAccessController[0]);
        }
    }

    /**
     * Initialize all elements of the plugin based on the configuration
     *
     * @return
     */
    boolean initializeConfig(IPlayer player) {
        m_isInitialized = false;
        m_ac.clear();
        m_loggers.clear();
        
        synchronized (m_mtaMutex) {
            player.say("Initializing access controllers");
            for (IAccessController ac : m_ac) {
                String acName = ac.getName();
                ac.reloadConfiguration();
                if (ac.isEnabled()) {
                    player.say(" * " + acName + "...enabled");
                } else {
                    player.say(" * " + acName + "...disabled");
                }
            }
            
            player.say("Initializing block loggers");
            for (IBlockLogger bl : m_loggers) {
                String blName = bl.getName();
                bl.reloadConfiguration();
                if (bl.isEnabled()) {
                    player.say(" * " + blName + "...enabled");
                } else {
                    player.say(" * " + blName + "...disabled");
                }
            }
        }
        m_isInitialized = true;
        return true;
    }
    
    void doShowStatus(IPlayer player) {
        if (!m_isInitialized) {
            player.say(Colors.RED + "Plugin not initialized.");
            return;
        }
        player.say(Colors.YELLOW + "Registered block loggers: ");
        for (IBlockLogger bl : m_loggers) {
            player.say(Colors.YELLOW + " * " + bl.getName() + "..." + (bl.isEnabled() ? "enabled" : "disabled"));
        }
        
        player.say(Colors.YELLOW + "Registered access controllers: ");
        for (IAccessController ac : m_ac) {
            player.say(Colors.YELLOW + " * " + ac.getName() + "..." + (ac.isEnabled() ? "enabled" : "disabled"));
        }
    }
    
    @Override
    public IPlayer getPlayer(String name) {
        return new LazyPlayer(name, m_platform);
    }

    /**
     * Gets the special blocks hub player instance
     *
     * @param uuid
     * @return
     */
    @Override
    public IPlayer getPlayer(UUID uuid) {
        return new LazyPlayer(uuid, m_platform);
    }

    /**
     * Get world based on the UUID
     *
     * @param uuid
     * @return
     */
    @Override
    public IWorld getWorld(UUID uuid) {
        return m_platform.getWorld(uuid);
    }

    /**
     * Get world based on name
     *
     * @param name
     * @return
     */
    @Override
    public IWorld getWorld(String name) {
        return m_platform.getWorld(name);
    }

    /**
     * Validates the logic state and the parameters
     *
     * @param location
     * @param params
     * @return
     */
    private boolean validate(IWorld world, Vector location, Object... params) {
        if (location == null || world == null || !m_isInitialized) {
            return false;
        }
        
        if (location.getY() < 0 || location.getY() >= world.getMaxHeight()) {
            return false;
        }
        
        for (Object o : params) {
            if (o == null) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void logBlock(Vector location, String player, String worldName, BlockData oldBlock, BlockData newBlock) {
        logBlock(location, getPlayer(player), getWorld(worldName), oldBlock, newBlock);
    }
    
    @Override
    public void logBlock(Vector location, UUID playerUuid, String worldName, BlockData oldBlock, BlockData newBlock) {
        logBlock(location, getPlayer(playerUuid), getWorld(worldName), oldBlock, newBlock);
    }
    
    @Override
    public void logBlock(Vector location, IPlayer player, String worldName, BlockData oldBlock, BlockData newBlock) {
        logBlock(location, player, getWorld(worldName), oldBlock, newBlock);
    }
    
    @Override
    public void logBlock(Vector location, String player, UUID worldUuid, BlockData oldBlock, BlockData newBlock) {
        logBlock(location, getPlayer(player), getWorld(worldUuid), oldBlock, newBlock);
    }
    
    @Override
    public void logBlock(Vector location, UUID playerUuid, UUID worldUuid, BlockData oldBlock, BlockData newBlock) {
        logBlock(location, getPlayer(playerUuid), getWorld(worldUuid), oldBlock, newBlock);
    }
    
    @Override
    public void logBlock(Vector location, IPlayer player, UUID worldUuid, BlockData oldBlock, BlockData newBlock) {
        logBlock(location, player, getWorld(worldUuid), oldBlock, newBlock);
    }
    
    @Override
    public void logBlock(Vector location, String player, IWorld world, BlockData oldBlock, BlockData newBlock) {
        logBlock(location, getPlayer(player), world, oldBlock, newBlock);
    }
    
    @Override
    public void logBlock(Vector location, UUID playerUuid, IWorld world, BlockData oldBlock, BlockData newBlock) {
        logBlock(location, getPlayer(playerUuid), world, oldBlock, newBlock);
    }
    
    @Override
    public void logBlock(Vector location, IPlayer player, IWorld world, BlockData oldBlock, BlockData newBlock) {
        if (!validate(world, location, player)) {
            return;
        }
        
        if (oldBlock == null) {
            oldBlock = BlockData.AIR;
        }
        if (newBlock == null) {
            newBlock = BlockData.AIR;
        }
        
        if (!ConfigProvider.isLogging(world.getName())) {
            return;
        }
        
        for (IBlockLogger bl : m_loggers) {
            try {
                bl.logBlock(location, player, world, oldBlock, newBlock);
            } catch (Exception ex) {
                ExceptionHelper.printException(ex, "Unable to log block change using " + bl.getName());
            }
        }
    }
    
    @Override
    public boolean hasAccess(String player, String worldName, Vector location) {
        return hasAccess(getPlayer(player), getWorld(worldName), location);
    }
    
    @Override
    public boolean hasAccess(UUID player, String worldName, Vector location) {
        return hasAccess(getPlayer(player), getWorld(worldName), location);
    }
    
    @Override
    public boolean hasAccess(IPlayer player, String worldName, Vector location) {
        return hasAccess(player, getWorld(worldName), location);
    }
    
    @Override
    public boolean hasAccess(String player, UUID worldUuid, Vector location) {
        return hasAccess(getPlayer(player), getWorld(worldUuid), location);
    }
    
    @Override
    public boolean hasAccess(UUID player, UUID worldUuid, Vector location) {
        return hasAccess(getPlayer(player), getWorld(worldUuid), location);
    }
    
    @Override
    public boolean hasAccess(IPlayer player, UUID worldUuid, Vector location) {
        return hasAccess(player, getWorld(worldUuid), location);
    }
    
    @Override
    public boolean hasAccess(String player, IWorld world, Vector location) {
        return hasAccess(getPlayer(player), world, location);
    }
    
    @Override
    public boolean hasAccess(UUID player, IWorld world, Vector location) {
        return hasAccess(getPlayer(player), world, location);
    }
    
    @Override
    public boolean hasAccess(IPlayer player, IWorld world, Vector location) {
        if (player == null || player.isConsole()) {
            return true;
        }
        
        if (!validate(world, location)) {
            return false;
        }
        
        for (IAccessController ac : m_ac) {
            try {
                if (!ac.hasAccess(player, world, location)) {
                    return false;
                }
            } catch (Exception ex) {
                ExceptionHelper.printException(ex, "Unable to check access controll using " + ac.getName());
            }
        }
        
        return true;
    }
    
    @Override
    public boolean canPlace(String player, String worldName, Vector location, BlockData oldBlock, BlockData newBlock) {
        return canPlace(getPlayer(player), getWorld(worldName), location, oldBlock, newBlock);
    }
    
    @Override
    public boolean canPlace(UUID player, String worldName, Vector location, BlockData oldBlock, BlockData newBlock) {
        return canPlace(getPlayer(player), getWorld(worldName), location, oldBlock, newBlock);
    }
    
    @Override
    public boolean canPlace(IPlayer player, String worldName, Vector location, BlockData oldBlock, BlockData newBlock) {
        return canPlace(player, getWorld(worldName), location, oldBlock, newBlock);
    }
    
    
    @Override
    public boolean canPlace(String player, UUID worldUuid, Vector location, BlockData oldBlock, BlockData newBlock) {
        return canPlace(getPlayer(player), getWorld(worldUuid), location, oldBlock, newBlock);
    }
    
    @Override
    public boolean canPlace(UUID player, UUID worldUuid, Vector location, BlockData oldBlock, BlockData newBlock) {
        return canPlace(getPlayer(player), getWorld(worldUuid), location, oldBlock, newBlock);
    }
    
    @Override
    public boolean canPlace(IPlayer player, UUID worldUuid, Vector location, BlockData oldBlock, BlockData newBlock) {
        return canPlace(player, getWorld(worldUuid), location, oldBlock, newBlock);
    }
    
    @Override
    public boolean canPlace(String player, IWorld world, Vector location, BlockData oldBlock, BlockData newBlock) {
        return canPlace(getPlayer(player), world, location, oldBlock, newBlock);
    }
    
    @Override
    public boolean canPlace(UUID player, IWorld world, Vector location, BlockData oldBlock, BlockData newBlock) {
        return canPlace(getPlayer(player), world, location, oldBlock, newBlock);
    }
    
    @Override
    public boolean canPlace(IPlayer player, IWorld world, Vector location, BlockData oldBlock, BlockData newBlock) {
        if (player == null || player.isConsole()) {
            return true;
        }
        
        if (!validate(world, location)) {
            return false;
        }
        
        for (IAccessController ac : m_ac) {
            try {
                if (!ac.canPlace(player, world, location, oldBlock, newBlock)) {
                    return false;
                }
            } catch (Exception ex) {
                ExceptionHelper.printException(ex, "Unable to check if block can be placed using " + ac.getName());
            }
        }
        
        return true;
    }

    @Override
    public void onEnable() {
        
    }

    @Override
    public void onDisable() {
        synchronized (m_mtaMutex) {
            m_ac.clear();
            m_loggers.clear();
        }
    }
    
}
