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
package org.primesoft.blockshub.accessors.bukkit.worldGuard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.primesoft.blockshub.api.IAccessController;
import org.primesoft.blockshub.api.IBlockData;
import org.primesoft.blockshub.api.ILog;
import org.primesoft.blockshub.api.IPlatform;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.IWorld;
import org.primesoft.blockshub.api.platform.base.BukkitBaseEntity;

/**
 *
 * @author SBPrime
 */
public class WorldGuardAc extends BukkitBaseEntity implements IAccessController {

    static IAccessController create(ILog logger, Object plugin, IPlatform platform) {
        if (!(plugin instanceof WorldGuardPlugin)) {
            logger.log("plugin not found.");
            return null;
        }

        return new WorldGuardAc((WorldGuardPlugin) plugin, platform);
    }

    private final Map<UUID, CacheEntry> m_cachedWorlds = new ConcurrentSkipListMap<>();

    private final RegionContainer m_rc;
    
    private final IPlatform m_platform;

    private WorldGuardAc(WorldGuardPlugin plugin, IPlatform platform) {
        super(plugin);
        
        m_platform = platform;
        m_rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
    }
    
    @Override
    public boolean hasAccess(IPlayer player,
            IWorld world, double x, double y, double z) {
        if (player == null || world == null) {
            return true;            
        }
                
        final Player bukkitPlayer = m_platform.getPlatformPlayer(player, Player.class);
        final Player bPlayer = bukkitPlayer != null ? bukkitPlayer.getPlayer() : null;   
        
        final LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(bPlayer);
        final CacheEntry ce = m_cachedWorlds.computeIfAbsent(world.getUuid(), id -> {
                    com.sk89q.worldedit.world.World w = BukkitAdapter.adapt(m_platform.getPlatformWorld(world, World.class));
                    return new CacheEntry(w, m_rc.get(w));
        });
        
        if (WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(lp, ce.world)) {
            return true;
        }
        
        final BlockVector3 bv = BlockVector3.at(x, y, z);
        return StateFlag.State.ALLOW.equals(ce.rm.getApplicableRegions(bv).queryValue(lp, Flags.BUILD));
    }

    @Override
    public boolean canPlace(IPlayer player,
            IWorld world, double x, double y, double z,
            IBlockData oldBlock, IBlockData newBlock) {
        
        return hasAccess(player, world, x, y, z);
    }
    
    private static class CacheEntry {
        final com.sk89q.worldedit.world.World world;
        final RegionManager rm;
        
        CacheEntry(com.sk89q.worldedit.world.World world, RegionManager rm) {
            this.world = world;
            this.rm = rm;
        }
    }
}
