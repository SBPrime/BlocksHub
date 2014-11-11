/*
 * BlocksHub a library plugin providing easy access to block loggers 
 * and block access controllers.
 * Copyright (c) 2014, SBPrime <https://github.com/SBPrime/>
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

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.PrimeSoft.blocksHub.GriefPrevention.GriefPrevention8x;
import org.PrimeSoft.blocksHub.GriefPrevention.GriefPreventionBase;
import org.PrimeSoft.blocksHub.accessControl.BaseAccessController;
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

    @Override
    public boolean reloadConfiguration() {
        return true;
    }
}
