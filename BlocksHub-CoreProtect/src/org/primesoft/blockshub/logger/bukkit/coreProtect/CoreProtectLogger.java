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
package org.primesoft.blockshub.logger.bukkit.coreProtect;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.primesoft.blockshub.api.BlockData;
import org.primesoft.blockshub.api.IBlockLogger;
import org.primesoft.blockshub.api.ILog;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.IWorld;
import org.primesoft.blockshub.api.Vector;
import org.primesoft.blockshub.platform.bukkit.BukkitBaseEntity;
import org.primesoft.blockshub.platform.bukkit.BukkitWorld;

/**
 *
 * @author SBPrime
 */
public class CoreProtectLogger extends BukkitBaseEntity implements IBlockLogger {

    /**
     * Create new instance of the logger
     *
     * @param platform
     * @return
     */
    static IBlockLogger create(ILog logger, Object plugin) {
        if (!(plugin instanceof CoreProtect)) {
            logger.log("plugin not found.");
            return null;
        }

        CoreProtect cp = (CoreProtect) plugin;        
        CoreProtectAPI coreProtect = cp.getAPI();
        if (coreProtect == null) {
            logger.log("unable to get CoreProtect API");
            return null;
        }
        
        int apiVersion = coreProtect.APIVersion();
        if (apiVersion < 3) {
            logger.log("unsupported plugin version");
            return null;
        }

        return new CoreProtectLogger(cp, coreProtect);
    }

    /**
     * Core protect
     */
    private final CoreProtectAPI m_coreProtect;

    public CoreProtectLogger(JavaPlugin plugin, CoreProtectAPI api) {
        super(plugin);
        
        m_coreProtect = api;
    }

    @Override
    public boolean reloadConfiguration() {
        return true;
    }

    @Override
    public void logBlock(Vector location, IPlayer player, IWorld world, BlockData oldBlock, BlockData newBlock) {
        if (!isEnabled()) {
            return;
        }

        if (!(world instanceof BukkitWorld)) {
            return;
        }

        Location l = new Location(((BukkitWorld) world).getWorld(), location.getX(), location.getY(), location.getZ());
        String playerName = player.getName();

        Material oldMaterial = Material.getMaterial(oldBlock.getType());
        Material newMaterial = Material.getMaterial(newBlock.getType());

        m_coreProtect.logRemoval(playerName, l, oldMaterial, (byte) oldBlock.getData());
        m_coreProtect.logPlacement(playerName, l, newMaterial, (byte) newBlock.getData());
    }
}
