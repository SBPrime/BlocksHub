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
package org.primesoft.blockshub.platform;

import java.util.UUID;
import org.primesoft.blockshub.api.IPlatform;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.Permissions;

/**
 *
 * @author SBPrime
 */
public class LazyPlayer implements IPlayer {

    private final UUID m_uuid;
    private final String m_name;
    private final IPlatform m_platform;
    private IPlayer m_resolved;

    /**
     * The MTA mutex
     */
    private final Object m_mutex = new Object();

    public LazyPlayer(UUID uuid, IPlatform platform) {
        m_uuid = uuid;
        m_name = null;
        m_resolved = null;
        m_platform = platform;
    }

    public LazyPlayer(String name, IPlatform platform) {
        m_uuid = null;
        m_name = name;
        m_resolved = null;
        m_platform = platform;
    }

    /**
     * Resolve the player
     */
    private void resolve() {
        if (m_resolved != null) {
            return;
        }

        synchronized (m_mutex) {
            if (m_resolved != null) {
                return;
            }

            IPlayer resolved;
            if (m_uuid != null) {
                resolved = m_platform.getPlayer(m_uuid);
            } else if (m_name != null && m_name.length() > 0) {
                resolved = m_platform.getPlayer(m_name);
            } else {
                resolved = null;
            }

            if (resolved == null) {
                resolved = ConsolePlayer.getInstance();
            }

            m_resolved = resolved;
        }
    }
    
    @Override
    public boolean isConsole() {
        resolve();
        return m_resolved.isConsole();
    }

    
    
    @Override
    public boolean isAllowed(Permissions node) {
        resolve();
        return m_resolved.isAllowed(node);
    }

    @Override
    public void say(String msg) {
        resolve();
        m_resolved.say(msg);
    }

    @Override
    public String getName() {
        if (m_name != null) {
            return m_name;
        }
        
        resolve();
        return m_resolved.getName();
    }

    @Override
    public UUID getUUID() {
        if (m_uuid != null) {
            return m_uuid;
        }
        
        resolve();
        return m_resolved.getUUID();
    }

    @Override
    public boolean equals(Object obj) {
        resolve();
        return m_resolved.equals(obj);
    }

    @Override
    public int hashCode() {
        resolve();
        return m_resolved.hashCode();
    }

    /**
     * Get the resolved player
     * @return 
     */
    public IPlayer getResolved() {
        resolve();
        return m_resolved;
    }
}
