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

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.PrimeSoft.blocksHub.accessControl.GriefPrevention.GriefPrevention7x;
import org.PrimeSoft.blocksHub.accessControl.GriefPrevention.GriefPrevention8x;
import org.PrimeSoft.blocksHub.accessControl.GriefPrevention.GriefPreventionBase;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author SBPrime
 */
public class GriefPreventionAc extends BaseAccessController<GriefPrevention> {

    private GriefPreventionBase m_interface;

    public GriefPreventionAc(JavaPlugin plugin) {
        super(plugin, "GriefPrevention");
    }

    @Override
    protected boolean postInit(PluginDescriptionFile pd) {
        try {
            m_interface = new GriefPrevention7x();
            if (m_interface.Initialize(m_hook))
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            //v7.x not supported try v8.x
        }
        
        
        try {
            m_interface = new GriefPrevention8x();
            if (m_interface.Initialize(m_hook))
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            //v8.x not supported
        }
        return false;
    }

    @Override
    public boolean canPlace(String player, World world, Location location) {
        if (!m_isEnabled || player == null || world == null || location == null) {
            return true;
        }
        Player bPlayer = m_server.getPlayer(player);
        
        if (m_interface == null || bPlayer == null)
        {
            return true;
        }
    
        return m_interface.canPlace(bPlayer, world, location);
    }

    @Override
    protected boolean instanceOfT(Class<? extends Plugin> aClass) {
        return GriefPrevention.class.isAssignableFrom(aClass);
    }
}
