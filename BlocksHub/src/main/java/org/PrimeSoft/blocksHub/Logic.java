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
package org.PrimeSoft.blocksHub;

import org.PrimeSoft.blocksHub.api.IBlockLogger;
import java.util.ArrayList;
import java.util.List;
import org.PrimeSoft.blocksHub.api.IAccessController;
import org.PrimeSoft.blocksHub.configuration.ConfigProvider;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author SBPrime
 */
public class Logic implements IBlocksHubApi {

    private final Object m_mtaMutex = new Object();
    private final List<IBlockLogger> m_loggers = new ArrayList<IBlockLogger>();
    private final List<IAccessController> m_ac = new ArrayList<IAccessController>();
    private boolean m_isInitialized;

    @Override
    public double getVersion() {
        return 1.0;
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
    boolean initializeConfig(Player player) {
        m_isInitialized = false;
        m_ac.clear();
        m_loggers.clear();

        synchronized (m_mtaMutex) {
            BlocksHub.say(player, "Initializing access controllers");
            for (IAccessController ac : m_ac) {
                String acName = ac.getName();
                ac.reloadConfiguration();
                if (ac.isEnabled()) {
                    BlocksHub.say(player, " * " + acName + "...enabled");
                } else {
                    BlocksHub.say(player, " * " + acName + "...disabled");
                }
            }

            BlocksHub.say(player, "Initializing block loggers");
            for (IBlockLogger bl : m_loggers) {
                String blName = bl.getName();
                bl.reloadConfiguration();
                if (bl.isEnabled()) {
                    BlocksHub.say(player, " * " + blName + "...enabled");
                } else {
                    BlocksHub.say(player, " * " + blName + "...disabled");
                }
            }
        }
        m_isInitialized = true;
        return true;
    }

    void doShowStatus(Player player) {
        if (!m_isInitialized) {
            BlocksHub.say(player, ChatColor.RED + "Plugin not initialized.");
            return;
        }
        BlocksHub.say(player, ChatColor.YELLOW + "Registered block loggers: ");
        for (IBlockLogger bl : m_loggers) {
            BlocksHub.say(player, ChatColor.YELLOW + " * " + bl.getName() + "..." + (bl.isEnabled() ? "enabled" : "disabled"));
        }

        BlocksHub.say(player, ChatColor.YELLOW + "Registered access controllers: ");
        for (IAccessController ac : m_ac) {
            BlocksHub.say(player, ChatColor.YELLOW + " * " + ac.getName() + "..." + (ac.isEnabled() ? "enabled" : "disabled"));
        }
    }

    @Override
    public void logBlock(String player, World world, Location location, int oldBlockType, byte oldBlockData, int newBlockType, byte newBlockData) {
        if (!m_isInitialized || world == null || location == null) {
            return;
        }

        final int y = location.getBlockY();
        if (!ConfigProvider.isLogging(world.getName()) || y < 0 || y > world.getMaxHeight()) {
            return;
        }

        for (IBlockLogger bl : m_loggers) {
            bl.logBlock(location, player, world, oldBlockType, oldBlockData, newBlockType, newBlockData);
        }
    }

    @Override
    public boolean canPlace(String player, World world, Location location) {
        if (player == null) {
            return true;
        }

        if (!m_isInitialized || world == null || location == null) {
            return false;
        }

        final int y = location.getBlockY();
        if (y < 0 || y >= world.getMaxHeight()) {
            return false;
        }
        for (IAccessController ac : m_ac) {
            if (!ac.canPlace(player, world, location)) {
                return false;
            }
        }

        return true;
    }
}
