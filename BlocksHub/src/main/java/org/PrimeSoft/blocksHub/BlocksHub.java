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

package org.PrimeSoft.blocksHub;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.PrimeSoft.blocksHub.configuration.ConfigProvider;
import org.PrimeSoft.blocksHub.mcstats.MetricsLite;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author SBPrime
 */
public class BlocksHub extends JavaPlugin {

    private static final Logger s_log = Logger.getLogger("Minecraft.BlocksHub");
    private static ConsoleCommandSender s_console;
    private static String s_prefix = null;
    private static String s_logFormat = "%s %s";
    private static BlocksHub s_instance;
    private MetricsLite m_metrics;
    private Logic m_logic;

    public static String getPrefix() {
        return s_prefix;
    }

    public BlocksHub getInstance() {
        return s_instance;
    }

    public static void log(String msg) {
        if (s_log == null || msg == null || s_prefix == null) {
            return;
        }

        s_log.log(Level.INFO, String.format(s_logFormat, s_prefix, msg));
    }

    public static void say(Player player, String msg) {
        if (player == null) {
            log(msg);
        } else {
            player.sendRawMessage(msg);
        }
    }

    @Override
    public void onEnable() {
        PluginDescriptionFile desc = getDescription();
        s_prefix = String.format("[%s]", desc.getName());

        m_logic = new Logic();

        try {
            m_metrics = new MetricsLite(this);
            m_metrics.start();
        } catch (IOException e) {
            log("Error initializing MCStats: " + e.getMessage());
        }

        s_console = getServer().getConsoleSender();

        if (!ConfigProvider.load(this)) {
            say(null, "Error loading config");
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
        log("Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (sender instanceof Player) ? (Player) sender : null;

        String commandName = command.getName();
        if (!commandName.equalsIgnoreCase(Commands.COMMAND_MAIN) &&
             !commandName.equalsIgnoreCase(Commands.COMMAND_MAIN2)) {
            return false;
        }

        String name = (args != null && args.length > 0) ? args[0] : "";


        if (name.equalsIgnoreCase(Commands.COMMAND_RELOAD)
                && PermissionManager.isAllowed(player, PermissionManager.Perms.ReloadConfig)) {
            doReloadConfig(player);
            return true;
        } else if (name.equalsIgnoreCase(Commands.COMMAND_STATUS)
                && PermissionManager.isAllowed(player, PermissionManager.Perms.ShowStatus)) {
            doShowStatus(player);
            return true;
        } else {
            doShowInfo(player);
            return true;
        }
    }

    private void doShowInfo(Player player) {
        final PluginDescriptionFile desc = getDescription();
        say(player, ChatColor.YELLOW + desc.getName() + " " + desc.getVersion());
    }

    private void doShowStatus(Player player) {
        doShowInfo(player);
        m_logic.doShowStatus(player);
    }

    /**
     * Do reload configuration command
     *
     * @param player
     */
    private void doReloadConfig(Player player) {
        if (player != null) {
            if (!PermissionManager.isAllowed(player, PermissionManager.Perms.ReloadConfig)) {
                say(player, ChatColor.RED + "You have no permissions to do that.");
                return;
            }
        }

        log(player != null ? player.getName() : "console " + " reloading config...");

        reloadConfig();

        if (!ConfigProvider.load(this)) {
            say(player, "Error loading config");
            return;
        }

        if (!m_logic.initializeConfig(player)) {
            say(player, "Error initializing config");
        }
    }

    /**
     * Get the API
     * @return 
     */
    public IBlocksHubApi getApi() {
        return m_logic;
    }
}