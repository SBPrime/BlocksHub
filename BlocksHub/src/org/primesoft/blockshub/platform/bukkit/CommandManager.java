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

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.SimpleCommandMap;
import org.primesoft.asyncworldedit.utils.Reflection;
import org.primesoft.blockshub.platform.api.ICommandManager;

/**
 *
 * @author SBPrime
 */
public class CommandManager implements ICommandManager {
   
    /**
     * The command map
     */
    private final SimpleCommandMap m_commandMap;
    
   /**
    * The plugin name
    */
    private final String m_pluginName;
    
    
    /**
     * The command executor
     */
    private final CommandExecutor m_executor;

    
    
    CommandManager(Server server, String pluginName, CommandExecutor executor) {
        m_pluginName = pluginName;
        m_commandMap = Reflection.get(server, SimpleCommandMap.class, "commandMap", "Unable to get the command map");
        m_executor = executor;
    }
    

    
    
    @Override
    public void registerCommand(String name, String[] alias, String description, String usage, String permission) {
        if (m_commandMap == null) {
            return;
        }
    
        SimpleCommand command = new SimpleCommand(name, description, usage, alias == null ?
                new ArrayList<String>(0) : Arrays.asList(alias), m_executor);
        if (permission != null && !permission.isEmpty()) {
            command.setPermission(permission);
        }
                
        m_commandMap.register(m_pluginName, command);
    }
}    
