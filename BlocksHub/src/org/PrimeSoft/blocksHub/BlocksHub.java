/*
 * The MIT License
 *
 * Copyright 2013 SBPrime.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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

        m_logic = new Logic(this);

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
     */
    public IBlocksHubApi getApi() {
        return m_logic;
    }
}