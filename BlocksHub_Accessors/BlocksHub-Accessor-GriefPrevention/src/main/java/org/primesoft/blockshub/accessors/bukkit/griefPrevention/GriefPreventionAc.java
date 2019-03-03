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
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.primesoft.blockshub.api.IAccessController;
import org.primesoft.blockshub.api.IBlockData;
import org.primesoft.blockshub.api.IPlatform;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.IWorld;
import org.primesoft.blockshub.api.platform.TypeOnlyBlock;
import org.primesoft.blockshub.api.platform.base.BukkitBaseEntity;

/**
 *
 * @author SBPrime
 */
public class GriefPreventionAc extends BukkitBaseEntity implements IAccessController {

    private final GriefPrevention m_griefPrevention;

    private final IPlatform m_platform;

    GriefPreventionAc(GriefPrevention griefPrevention, IPlatform platform) {
        super(griefPrevention, "GriefPrevention");

        m_platform = platform;
        m_griefPrevention = griefPrevention;
    }

    /**
     * Get the player data
     *
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
    public boolean hasAccess(IPlayer player, IWorld world, double x, double y, double z) {
        if (world == null) {
            return false;
        }
        if (player == null) {
            return true;
        }

        final World bWorld = m_platform.getPlatformWorld(world, World.class);
        final Player bukkitPlayer = m_platform.getPlatformPlayer(player, Player.class);

        if (bWorld == null || bukkitPlayer == null) {
            return true;
        }

        if (!m_griefPrevention.claimsEnabledForWorld(bWorld)) {
            return true;
        }

        final PlayerData playerData = getPlayerData(player);
        if (playerData == null || playerData.ignoreClaims) {
            return true;
        }

        final Location l = m_platform.getPlatformLocation(world, x, y, z, Location.class);
        final Claim claim = m_griefPrevention.dataStore.getClaimAt(l, true, playerData.lastClaim);

        playerData.lastClaim = claim;

        return (claim == null) || (claim.allowAccess(bukkitPlayer) == null);
    }

    @Override
    public boolean canPlace(IPlayer player, IWorld world,
            double x, double y, double z,
            IBlockData blockOld, IBlockData blockNew) {
        if (world == null) {
            return false;
        }
        if (player == null) {
            return true;
        }

        final boolean airOld = blockOld.isAir();
        final boolean airNew = blockNew.isAir();

        if (airOld && airNew) {
            return true;
        }

        final World bWorld = m_platform.getPlatformWorld(world, World.class);
        final Player bukkitPlayer = m_platform.getPlatformPlayer(player, Player.class);

        if (bWorld == null || bukkitPlayer == null) {
            return true;
        }

        final Location l = m_platform.getPlatformLocation(world, x, y, z, Location.class);

        if (!airOld) {
            if (m_griefPrevention.allowBreak(bukkitPlayer, new TypeOnlyBlock(blockOld), l) != null) {
                return false;
            }
        }
        
        return m_griefPrevention.allowBuild(bukkitPlayer, l, blockOld.getData(BlockData.class).getMaterial()) == null;
    }
}
