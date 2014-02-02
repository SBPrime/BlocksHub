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
package org.PrimeSoft.blocksHub.accessControl;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author SBPrime
 */
public class WorldGuardAc implements IAccessController {
    /**
     * AC name
     */
    private final String m_name;
    
    
    /**
     * The world guard
     */
    private WorldGuardPlugin m_worldGuard;

    /**
     * Craftbukkit server
     */
    private Server m_server;

    /**
     * Is world guard integration enabed
     */
    private boolean m_isEnabled;

    public WorldGuardAc(JavaPlugin plugin) {
        m_isEnabled = false;
        PluginDescriptionFile pd = null;
        try {
            Plugin cPlugin = plugin.getServer().getPluginManager().getPlugin("WorldGuard");

            if ((cPlugin != null) && (cPlugin instanceof WorldGuardPlugin)) {
                m_isEnabled = true;
                m_worldGuard = (WorldGuardPlugin) cPlugin;
                m_server = plugin.getServer();
                
                pd = m_worldGuard.getDescription();                
            }
        }
        catch (NoClassDefFoundError ex) {
        }
        
        m_name = pd != null ? pd.getFullName() : "Disabled - WorldGuard";
    }

    /**
     * /**
     * Check if a player is allowed to place a block
     *
     * @param player
     * @param location
     * @param world
     * @return
     */
    @Override
    public boolean canPlace(String player, World world, Location location) {
        if (!m_isEnabled || player == null) {
            return true;
        }

        Player p = m_server.getPlayer(player);
        return m_worldGuard.canBuild(p, location);
    }

    @Override
    public boolean isEnabled() {
        return m_isEnabled;
    }
        
    /**
     * Get access controller name
     * @return 
     */
    @Override
    public String getName() {
        return m_name;
    }
}
