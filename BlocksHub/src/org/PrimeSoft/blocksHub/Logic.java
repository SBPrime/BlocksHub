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
package org.PrimeSoft.blocksHub;

import java.util.ArrayList;
import java.util.List;
import org.PrimeSoft.blocksHub.accessControl.AccessControllers;
import org.PrimeSoft.blocksHub.accessControl.IAccessController;
import org.PrimeSoft.blocksHub.accessControl.WorldGuardAc;
import org.PrimeSoft.blocksHub.blocklogger.*;
import org.PrimeSoft.blocksHub.configuration.ConfigProvider;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author SBPrime
 */
public class Logic implements IBlocksHubApi {

    private final List<IBlockLogger> m_loggers = new ArrayList<IBlockLogger>();
    private final List<IAccessController> m_ac = new ArrayList<IAccessController>();
    private final JavaPlugin m_parent;
    private boolean m_isInitialized;

    public Logic(JavaPlugin parent) {
        m_parent = parent;
    }

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
     * Initialize all elements of the plugin based on the configuration
     *
     * @return
     */
    boolean initializeConfig(Player player) {
        m_isInitialized = false;
        m_ac.clear();
        m_loggers.clear();

        BlocksHub.say(player, "Initializing access controllers");
        for (String acName : ConfigProvider.getAccessControlers()) {
            IAccessController ac = getAccessController(acName);
            if (ac == null) {
                BlocksHub.say(player, " * " + acName + "...error");
            } else if (ac.isEnabled()) {
                m_ac.add(ac);
                BlocksHub.say(player, " * " + acName + "...initialized");
            } else {
                BlocksHub.say(player, " * " + acName + "...not found");
            }
        }

        BlocksHub.say(player, "Initializing block loggers");
        for (String blName : ConfigProvider.getLoggers()) {
            IBlockLogger bc = getLogger(blName);
            if (bc == null) {
                BlocksHub.say(player, " * " + blName + "...error");
            } else if (bc.isEnabled()) {
                m_loggers.add(bc);
                BlocksHub.say(player, " * " + blName + "...initialized");
            } else {
                BlocksHub.say(player, " * " + blName + "...not found");
            }
        }

        m_isInitialized = true;
        return true;
    }

    /**
     * Create the block logger
     *
     * @param logger
     * @return
     */
    private IBlockLogger getLogger(String logger) {
        try {
            if (logger != null) {
                if (logger.equalsIgnoreCase(Loggers.LOG_BLOCK)) {
                    return new LogBlockLogger(m_parent);
                }
                if (logger.equalsIgnoreCase(Loggers.CORE_PROTECT)) {
                    return new CoreProtectLogger(m_parent);
                }
                if (logger.equalsIgnoreCase(Loggers.PRISM)) {
                    return new PrismLogger(m_parent);
                }
                if (logger.equalsIgnoreCase(Loggers.HAWK_EYE)) {
                    return new HawkEyeLogger(m_parent);
                }
            }
            return null;
        } catch (NoClassDefFoundError ex) {
            return null;
        }
    }

    /**
     * Create the access controller
     *
     * @param name
     * @return
     */
    private IAccessController getAccessController(String name) {
        if (name != null) {
            if (name.equalsIgnoreCase(AccessControllers.WORLD_GUARD)) {
                return new WorldGuardAc(m_parent);
            }
        }
        return null;
    }

    void doShowStatus(Player player) {
        if (!m_isInitialized) {
            BlocksHub.say(player, ChatColor.RED + "Plugin not initialized.");
            return;
        }
        BlocksHub.say(player, ChatColor.YELLOW + "Enabled block loggers: ");
        for (IBlockLogger bl : m_loggers) {
            BlocksHub.say(player, ChatColor.YELLOW + " * " + bl.getName());
        }

        BlocksHub.say(player, ChatColor.YELLOW + "Enabled access controllers: ");
        for (IAccessController ac : m_ac) {
            BlocksHub.say(player, ChatColor.YELLOW + " * " + ac.getName());
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
        if (player == null)
        {
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
