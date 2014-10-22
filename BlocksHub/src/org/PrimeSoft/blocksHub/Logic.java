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

import java.util.ArrayList;
import java.util.List;
import org.PrimeSoft.blocksHub.accessControl.AccessControllers;
import org.PrimeSoft.blocksHub.accessControl.GriefPreventionAc;
import org.PrimeSoft.blocksHub.accessControl.IAccessController;
import org.PrimeSoft.blocksHub.accessControl.ResidenceAc;
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
        for (AccessControllers acType : ConfigProvider.getAccessControlers()) {
            String acName = acType.getName();
            IAccessController ac = getAccessController(acType);
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
        for (Loggers blType : ConfigProvider.getLoggers()) {
            String blName = blType.getName();
            IBlockLogger bc = getLogger(blType);
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
    private IBlockLogger getLogger(Loggers logger) {
        try {
            switch (logger) {
                case LOG_BLOCK:
                    return new LogBlockLogger(m_parent);
                case CORE_PROTECT:
                    return new CoreProtectLogger(m_parent);
                case PRISM:
                    return new PrismLogger(m_parent);
                case HAWK_EYE:
                    return new HawkEyeLogger(m_parent);
                default:
                    return null;
            }
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
    private IAccessController getAccessController(AccessControllers name) {
        if (name != null) {
            switch (name) {
                case WORLD_GUARD:
                    return new WorldGuardAc(m_parent);
                case RESIDENCE:
                    return new ResidenceAc(m_parent);
                case GRIEF_PREVENTION:
                    return new GriefPreventionAc(m_parent);
                default:
                    return null;
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
