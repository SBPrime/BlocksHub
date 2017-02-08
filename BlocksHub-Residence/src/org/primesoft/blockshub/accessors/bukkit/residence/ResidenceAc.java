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
package org.primesoft.blockshub.accessors.bukkit.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.ResidenceCommandListener;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidenceManager;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.UUID;
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
import org.primesoft.blockshub.platform.bukkit.BukkitWorld;

/**
 *
 * @author SBPrime
 */
public class ResidenceAc extends BukkitBaseEntity implements IAccessController {

    static IAccessController create(ILog logger, Object plugin) {
        if (!(plugin instanceof ResidenceCommandListener)) {
            logger.log("plugin not found.");
            return null;
        }

        ResidenceManager manager;
        try {
            manager = Residence.getResidenceManager();
        } catch (Error ex) {
            manager = null;
        }
        
        if (manager == null) {
            logger.log("unable to get residence manager");
            return null;
        }

        return new ResidenceAc((ResidenceCommandListener) plugin, manager);
    }

    /**
     * The residence manager
     */
    private ResidenceManager m_manager;

    public ResidenceAc(ResidenceCommandListener plugin, ResidenceManager manager) {
        super(plugin);

        m_manager = manager;
    }

    private ClaimedResidence getResidence(IWorld world, Vector location) {
        World bWorld = ((BukkitWorld) world).getWorld();
        if (bWorld == null) {
            return null;
        }

        Location l = new Location(bWorld, location.getX(), location.getY(), location.getZ());
        return m_manager.getByLoc(l);
    }

    @Override
    public boolean hasAccess(IPlayer player, IWorld world, Vector location) {
        ClaimedResidence residence = getResidence(world, location);
        if (residence == null) {
            return true;
        }

        return hasPlayer(residence, player);
    }

    /**
     * Checks if residence contains player
     *
     * @param residence
     * @param player
     * @return
     */
    private boolean hasPlayer(ClaimedResidence residence, IPlayer player) {
        List<Player> players = residence.getPlayersInResidence();
        if (players == null || players.isEmpty()) {
            return false;
        }

        UUID uuid = player.getUUID();
        for (Player p : players) {
            if (p.getUniqueId().equals(uuid)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canPlace(IPlayer player, IWorld world, Vector location,
            BlockData blockOld, BlockData blockNew) {
        boolean airOld = blockOld.isAir();
        boolean airNew = blockNew.isAir();

        if (airNew && airOld) {
            return true;
        }

        ClaimedResidence residence = getResidence(world, location);
        if (residence == null) {
            return true;
        }

        if (!airNew && 
            !residence.getItemBlacklist().isAllowed(Material.getMaterial(blockNew.getType()))) {
            return false;
        }
        
        boolean hasBuild = residence.getPermissions().playerHas(player.getName(), world.getName(), "build", true);

        String flag = (airNew) ? "destroy" : "place";
        boolean isAllowed = residence.getPermissions().playerHas(player.getName(), world.getName(), flag,hasBuild);
        
        return isAllowed;
    }
}
