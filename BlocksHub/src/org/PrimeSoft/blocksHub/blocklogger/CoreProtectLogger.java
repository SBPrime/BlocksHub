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

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
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
public class CoreProtectLogger implements IBlockLogger {

    /**
     * Plugin name
     */
    private final String m_name;
    /**
     * Core protect
     */
    private CoreProtectAPI m_coreProtect;
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
    public static CoreProtect getCoreProtect(JavaPlugin plugin) {
        try {
            Plugin cPlugin = plugin.getServer().getPluginManager().getPlugin("CoreProtect");

            if ((cPlugin == null) || (!(cPlugin instanceof CoreProtect))) {
                return null;
            }

            return (CoreProtect) cPlugin;
        } catch (NoClassDefFoundError ex) {
            return null;
        }
    }

    public CoreProtectLogger(JavaPlugin plugin) {
        PluginDescriptionFile pd = null;
        CoreProtect cp = getCoreProtect(plugin);
        m_coreProtect = cp != null ? cp.getAPI() : null;
        if (m_coreProtect == null) {
            m_isEnabled = false;
        } else {
            m_isEnabled = true;
            pd = cp.getDescription();
        }
        
        m_name = pd != null ? pd.getFullName() : "Disabled - CoreProtect";
    }

    @Override
    public void logBlock(Location location, String player, World world,
            int oldBlockType, byte oldBlockData, int newBlockType,
            byte newBlockData) {
        if (!m_isEnabled) {
            return;
        }

        Location l = new Location(world, location.getBlockX(), location.getBlockY(), location.getBlockZ());
        m_coreProtect.logRemoval(player, l, oldBlockType, oldBlockData);
        m_coreProtect.logPlacement(player, l, newBlockType, newBlockData);
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
