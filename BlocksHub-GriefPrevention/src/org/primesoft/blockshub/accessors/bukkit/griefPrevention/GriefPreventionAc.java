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
package org.primesoft.blockshub.accessors.bukkit.griefPrevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.primesoft.blockshub.api.BlockData;
import org.primesoft.blockshub.api.IAccessController;
import org.primesoft.blockshub.api.ILog;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.IWorld;
import org.primesoft.blockshub.api.Vector;
import org.primesoft.blockshub.platform.bukkit.BukkitBaseEntity;
import org.primesoft.blockshub.platform.bukkit.BukkitPlayer;
import org.primesoft.blockshub.platform.bukkit.BukkitWorld;
import org.primesoft.blockshub.platform.bukkit.TypeOnlyBlock;

/**
 *
 * @author SBPrime
 */
public class GriefPreventionAc extends BukkitBaseEntity implements IAccessController {

    static IAccessController create(ILog logger, Object plugin) {
        if (!(plugin instanceof GriefPrevention)) {
            logger.log("plugin not found.");
            return null;
        }
        
        return new GriefPreventionAc((GriefPrevention)plugin);
    }
    
    
    private final GriefPrevention m_griefPrevention;

    
    public GriefPreventionAc(GriefPrevention griefPrevention) {
        super(griefPrevention, "GriefPrevention");
        
        m_griefPrevention = griefPrevention;
    }    
    
    /**
     * Get the player data
     * @param player
     * @return 
     */
    private PlayerData getPlayerData(IPlayer player) {
        if (player == null) {
            return null;
        }
        
        return m_griefPrevention.dataStore.getPlayerData(player.getUUID());
    }

    @Override
    public boolean hasAccess(IPlayer player, IWorld world, Vector location) {
        if (location == null || !(world instanceof BukkitWorld)) {
            return true;
        }
        
        World bWorld = ((BukkitWorld)world).getWorld();
        BukkitPlayer bPlayer = BukkitPlayer.getPlayer(player);        
            
        if (bWorld == null || bPlayer == null) {
            return true;
        }
        
        Player bukkitPlayer = bPlayer.getPlayer();
        if (bukkitPlayer == null) {
            return true;
        }
        
        if (!m_griefPrevention.claimsEnabledForWorld(bWorld)) {
            return true;
        }

        PlayerData playerData = getPlayerData(player);
        if (playerData == null || playerData.ignoreClaims || 
                m_griefPrevention.config_mods_ignoreClaimsAccounts.contains(player.getName())) {
            return true;
        }
        
        Location l = new Location(bWorld, location.getX(), location.getY(), location.getZ());
        Claim claim = m_griefPrevention.dataStore.getClaimAt(l, true, playerData.lastClaim);
        
        playerData.lastClaim = claim;        
                        
        return (claim == null) || (claim.allowAccess(bukkitPlayer) == null);
    }

    @Override
    public boolean canPlace(IPlayer player, IWorld world, Vector location, 
            BlockData blockOld, BlockData blockNew) {
        boolean airOld = blockOld.isAir();
        boolean airNew = blockNew.isAir();
        
        if (airOld && airNew) {
            return true;
        }
        
        if (location == null || !(world instanceof BukkitWorld)) {
            return true;
        }
        
        World bWorld = ((BukkitWorld)world).getWorld();
        BukkitPlayer bPlayer = BukkitPlayer.getPlayer(player);        
            
        if (bWorld == null || bPlayer == null) {
            return true;
        }
        
        Player bukkitPlayer = bPlayer.getPlayer();
        if (bukkitPlayer == null) {
            return true;
        }
        
        Location l = new Location(bWorld, location.getX(), location.getY(), location.getZ());               
        
        if (airOld) {
            if (m_griefPrevention.allowBreak(bukkitPlayer, new TypeOnlyBlock(blockOld), l) != null) {
                return false;
            }
        }
        
        return m_griefPrevention.allowBuild(bukkitPlayer, l, Material.getMaterial(blockOld.getType())) == null;
    }
}
