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
package org.primesoft.blockshub;

import static org.primesoft.blockshub.LoggerProvider.log;
import static org.primesoft.blockshub.LoggerProvider.sayConsole;
import org.primesoft.blockshub.configuration.ConfigProvider;
import org.primesoft.blockshub.platform.api.Colors;
import org.primesoft.blockshub.platform.api.ICommandManager;
import org.primesoft.blockshub.platform.api.IEnableAware;
import org.primesoft.blockshub.platform.api.IPlatform;
import org.primesoft.blockshub.api.IPlayer;

/**
 *
 * @author SBPrime
 */
public final class BlocksHubCore implements IEnableAware, IBlocksHubApiProvider {
    private final IPlatform m_platform;
    
    private final Logic m_logic;

    BlocksHubCore(IPlatform platform) {
        m_platform = platform;
        m_platform.initialize(this);
        
        m_logic = new Logic(platform);        
    }            

    @Override
    public void onEnable() {
        m_platform.onEnable();
        initializeCommands(m_platform.getCommandManager());
        
        if (!ConfigProvider.load(m_platform)) {
            sayConsole("Error loading config");
            return;
        }
        if (!ConfigProvider.isConfigUpdated()) {
            log("Please update your config file!");
        }

        if (!m_logic.initializeConfig(null)) {
            log("Error initializing config");
            return;
        }
        
        log("Enabled");

    }

    @Override
    public void onDisable() {
        m_platform.onDisable();
        
        log("Disabled");
    }        
    
    /**
     * Handle the command
     * @param player
     * @param commandName
     * @param args
     * @return 
     */
    public boolean onCommand(IPlayer player, String commandName, String[] args) {        
        if (!commandName.equalsIgnoreCase(Commands.COMMAND_MAIN) &&
             !commandName.equalsIgnoreCase(Commands.COMMAND_MAIN2)) {
            return false;
        }

        String name = (args != null && args.length > 0) ? args[0] : "";


        if (name.equalsIgnoreCase(Commands.COMMAND_RELOAD)
                && player.isAllowed(Permissions.ReloadConfig)) {
            doReloadConfig(player);
            return true;
        } else if (name.equalsIgnoreCase(Commands.COMMAND_STATUS)
                && player.isAllowed(Permissions.ShowStatus)) {
            doShowStatus(player);
            return true;
        } else {
            doShowInfo(player);
            return true;
        }
    }

    private void doShowInfo(IPlayer player) {
        IPlatform platform = m_platform;
        if (player == null || platform == null) {
            return;
        }
        
        player.say(String.format("%3$sBlocksHub %1$s %2$s", platform.getName(), platform.getVersion(), Colors.YELLOW));
    }

    private void doShowStatus(IPlayer player) {
        if (player == null) {
            return;
        }
        
        doShowInfo(player);
        m_logic.doShowStatus(player);
    }

    /**
     * Do reload configuration command
     *
     * @param player
     */
    private void doReloadConfig(IPlayer player) {
        if (player == null) {
            return;
        }
        
        if (!player.isAllowed(Permissions.ReloadConfig)) {
            player.say(Colors.RED + "You have no permissions to do that.");
            return;
        }

        log(player.getName() + " reloading config...");

        
        m_platform.reloadConfig();

        if (!ConfigProvider.load(m_platform)) {
            player.say("Error loading config");
            return;
        }

        if (!m_logic.initializeConfig(player)) {
            player.say("Error initializing config");
        }
    }

    @Override
    public IBlocksHubApi getApi() {
        return m_logic;
    }

    /**
     * Initialize the BlocksHub commands
     * @param commandManager 
     */
    private void initializeCommands(ICommandManager commandManager) {
        if (commandManager == null) {
            return;
        }
        
        commandManager.registerCommand("bh", new String[]{ "BlocksHub" }, 
                "BlocksHub plugin main command. Displays the help for BlocksHub.", 
                "/blocksHub", null);
    }
}
