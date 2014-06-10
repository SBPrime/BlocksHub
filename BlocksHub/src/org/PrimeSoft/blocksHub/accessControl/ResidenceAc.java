/*
 * The MIT License
 *
 * Copyright 2014 SBPrime.
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
package org.PrimeSoft.blocksHub.accessControl;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.listeners.ResidenceBlockListener;
import org.PrimeSoft.blocksHub.accessControl.IAccessController;
import org.PrimeSoft.blocksHub.SilentPlayer;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author SBPrime
 */
public class ResidenceAc implements IAccessController {
    /**
     * AC name
     */
    private final String m_name;

    /**
     * Craftbukkit server
     */
    private Server m_server;

    /**
     * Is world guard integration enabed
     */
    private boolean m_isEnabled;

    /**
     * The residence block listener
     */
    private ResidenceBlockListener m_hook;

    @Override
    public boolean isEnabled() {
        return m_isEnabled;
    }

    @Override
    public String getName() {
        return m_name;
    }

    public ResidenceAc(JavaPlugin plugin) {
        m_isEnabled = false;
        PluginDescriptionFile pd = null;
        try {
            Plugin cPlugin = plugin.getServer().getPluginManager().getPlugin("Residence");

            if ((cPlugin != null) && (cPlugin instanceof Residence)) {
                m_isEnabled = true;
                Residence residence = (Residence) cPlugin;
                m_hook = new ResidenceBlockListener();

                m_server = plugin.getServer();

                pd = residence.getDescription();
            } else {
                m_hook = null;
            }
        }
        catch (NoClassDefFoundError ex) {
            m_hook = null;
        }

        m_name = pd != null ? pd.getFullName() : "Disabled - Residence";
    }

    @Override
    public boolean canPlace(String player, World world, Location location) {
        if (!m_isEnabled || player == null || world == null || location == null) {
            return true;
        }

        Player bPlayer = m_server.getPlayer(player);
        if (bPlayer == null)
        {
            return true;
        }
        bPlayer = new SilentPlayer(bPlayer);
        Block block = location.getBlock();
        
        if (!block.isEmpty()) {
            BlockBreakEvent event = new BlockBreakEvent(block, bPlayer);
            m_hook.onBlockBreak(event);

            if (event.isCancelled()) {
                return false;
            }
        } else {
            BlockPlaceEvent event = new BlockPlaceEvent(block, block.getState(), block, 
                    bPlayer.getItemInHand(), bPlayer, true);
            m_hook.onBlockPlace(event);
            
            if (event.isCancelled()) {
                return false;
            }
        }
        
        //We do not support white/black lists
        return true;
    }
}
