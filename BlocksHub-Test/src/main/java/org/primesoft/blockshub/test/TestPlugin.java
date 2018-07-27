/*
 * BlocksHub a library plugin providing easy access to block loggers 
 * and block access controllers.
 * Copyright (c) 2017, SBPrime <https://github.com/SBPrime/>
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
package org.primesoft.blockshub.test;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.primesoft.blockshub.IBlocksHubApi;
import org.primesoft.blockshub.IBlocksHubApiProvider;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.platform.BukkitBlockData;

/**
 *
 * @author SBPPrime
 */
public class TestPlugin extends JavaPlugin {

    private IBlocksHubApi m_api;

    @Override
    public void onEnable() {
        super.onEnable();

        IBlocksHubApiProvider apiProvider = (IBlocksHubApiProvider) getServer().getPluginManager().getPlugin("BlocksHub");

        m_api = apiProvider.getApi();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        
        if (command == null || !command.getName().equalsIgnoreCase("bctest")) {
            return false;
        }

        Player player = (Player) sender;

        World world = player.getWorld();
        Location l = player.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();

        IPlayer bcPlayer = m_api.getPlayer(player.getUniqueId());
        if (y < 0 || y >= 255) {
            return true;
        }

        boolean hasAccess = m_api.hasAccess(bcPlayer, world.getUID(),
                x, y - 1, z);
        
        player.sendRawMessage("Has access: " + hasAccess);

        ItemStack itemStack = player.getItemOnCursor();        
        
        if (y < 1 || itemStack == null) {
            return true;
        }
        
        Block b = world.getBlockAt(x, y - 1, z);
        hasAccess = m_api.canPlace(bcPlayer, world.getUID(), 
                x, y - 1, z, 
                new BukkitBlockData(b.getBlockData()),
                new BukkitBlockData(Bukkit.createBlockData(itemStack.getType())));
        
        player.sendRawMessage("Can place: " + hasAccess);
        
        return true;
    }
}
