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

package org.PrimeSoft.blocksHub.griefPrevention8x;

import org.PrimeSoft.blocksHub.api.IBlocksHubApi;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.PrimeSoft.blocksHub.BlocksHub;
import org.PrimeSoft.blocksHub.api.IAccessController;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author SBPrime
 */
public class BlocksHubPlugin extends JavaPlugin {
    private static final Logger s_log = Logger.getLogger("Minecraft.BlocksHub.Ac.GriefPrevention");
    private static String s_prefix = null;
    private static final String s_logFormat = "%s %s";
    /**
     * THe blocks hub instance
     */
    private BlocksHub m_blocksHub;
    
    /**
     * The blocks hub api
     */
    private IBlocksHubApi m_api;
    
    /**
     * The access controller class
     */
    private IAccessController m_ac;

    /**
     * Get instance of the BlocksHub plugin
     *
     * @param plugin
     * @return
     */
    public static BlocksHub getBlocksHub(JavaPlugin plugin) {
        try {
            Plugin cPlugin = plugin.getServer().getPluginManager().getPlugin("BlocksHub");

            if ((cPlugin == null) || (!(cPlugin instanceof BlocksHub))) {
                return null;
            }

            return (BlocksHub) cPlugin;
        } catch (Exception ex) {
            return null;
        }
    }

    public static void log(String msg) {
        if (s_log == null || msg == null || s_prefix == null) {
            return;
        }

        s_log.log(Level.INFO, String.format(s_logFormat, s_prefix, msg));
    }
    
    public IAccessController CreateLogger() {
        try {
            return new GriefPreventionAc(this);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void onEnable() {
        PluginDescriptionFile desc = getDescription();
        s_prefix = String.format("[%s]", desc.getName());

        m_ac = CreateLogger();        
        if (m_ac == null) {
            log("Error initializeng");
            return;
        } else if (!m_ac.isEnabled()) {
            log("logger not found");
            return;
        }
        
        m_blocksHub = getBlocksHub(this);
        if (m_blocksHub == null)
        {
            log("BlocksHub plugin not found");
            return;
        }
        
        m_api = m_blocksHub.getApi();
        if (m_api == null)
        {
            log("Unable to get BlocksHub API");
            return;
        }
        
        m_api.registerAccessController(m_ac);        
        log("Enabled");
    }

    @Override
    public void onDisable() {
        if (m_blocksHub != null &&
                m_api != null &&
                m_ac != null)
        {
            m_api.removeAccessController(m_ac);
        }
        log("Disabled");
    }
}