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

import org.PrimeSoft.blocksHub.api.IBlockLogger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.HawkEye;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.entry.BlockChangeEntry;
import uk.co.oliwali.HawkEye.entry.BlockEntry;

/**
 *
 * @author SBPrime
 */
public class HawkEyeLogger implements IBlockLogger {

    /**
     * Plugin name
     */
    private final String m_name;
    /**
     * HawkEye protect
     */
    private HawkEye m_hawkEye;
    /**
     * Is the logger enabled
     */
    private boolean m_isEnabled;

    /**
     * Get instance of the core protect plugin
     *
     * @param plugin
     * @return
     */
    public static HawkEye getHawkEye(JavaPlugin plugin) {
        try {
            Plugin cPlugin = plugin.getServer().getPluginManager().getPlugin("HawkEye");

            if ((cPlugin == null) || (!(cPlugin instanceof HawkEye))) {
                return null;
            }

            return (HawkEye) cPlugin;
        } catch (NoClassDefFoundError ex) {
            return null;
        }
    }

    public HawkEyeLogger(JavaPlugin plugin) {
        PluginDescriptionFile pd = null;
        m_hawkEye = getHawkEye(plugin);
        if (m_hawkEye == null) {
            m_isEnabled = false;
        } else {
            m_isEnabled = true;
            pd = m_hawkEye.getDescription();
        }
        
        m_name = pd != null ? pd.getFullName() : "Disabled - HawkEye";
    }

    @Override
    public void logBlock(Location location, String player, World world,
            int oldBlockType, byte oldBlockData, int newBlockType,
            byte newBlockData) {

        if (!m_isEnabled) {
            return;
        }

        Location l = new Location(world, location.getBlockX(), location.getBlockY(), location.getBlockZ());

        if (newBlockType == Material.AIR.getId()) {
            DataManager.addEntry(new BlockEntry(player, DataType.WORLDEDIT_BREAK, oldBlockType, oldBlockData, l));
        } else {
            DataManager.addEntry(new BlockChangeEntry(player, DataType.WORLDEDIT_PLACE, l,
                    oldBlockType, oldBlockData,
                    newBlockType, newBlockData));
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
}