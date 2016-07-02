/*
 * BlocksHub a library plugin providing easy access to block loggers 
 * and block access controllers.
 * Copyright (c) 2016, SBPrime <https://github.com/SBPrime/>
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
package org.primesoft.blockshub.platform.bukkit;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.primesoft.blockshub.Permissions;
import org.primesoft.blockshub.api.IPlayer;

/**
 *
 * @author SBPrime
 */
public class BukkitPlayer implements IPlayer {
    private final Player m_player;
    
    public BukkitPlayer(Player player) {
        m_player = player;
    }

    @Override
    public boolean isAllowed(Permissions node) {
        if (node == null) {
            return true;
        }
        
        return m_player.hasPermission(node.getNode());
    }
    
    public Player getPlayer() {
        return m_player;
    }

    @Override
    public void say(String msg) {
        m_player.sendRawMessage(msg);
    }

    @Override
    public String getName() {
        return m_player.getName();
    }

    @Override
    public UUID getUUID() {
        return m_player.getUniqueId();
    }

    @Override
    public boolean isConsole() {
        return false;
    }
    
    

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BukkitPlayer)) {
            return false;
        }
        
        BukkitPlayer other = (BukkitPlayer)obj;
        
        return getUUID().equals(other.getUUID());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.m_player != null ? this.m_player.hashCode() : 0);
        return hash;
    }
}
