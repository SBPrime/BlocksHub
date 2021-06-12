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

import java.io.IOException;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.primesoft.blockshub.platform.ConsolePlayer;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.IWorld;
import static org.primesoft.blockshub.utils.LoggerProvider.log;
import org.primesoft.blockshub.api.platform.ILogger;
import org.primesoft.blockshub.inner.api.IBlocksHubCore;
import org.primesoft.blockshub.inner.api.platform.Colors;
import org.primesoft.blockshub.inner.api.platform.ICommandManager;
import org.primesoft.blockshub.inner.api.platform.IConfiguration;
import org.primesoft.blockshub.inner.api.platform.IPlatform;
import org.primesoft.blockshub.platform.LazyPlayer;

/**
 *
 * @author SBPrime
 */
public class BukkitPlatform implements IPlatform, CommandExecutor {

    private final static String PLATFORM_NAME = "Bukkit";

    /**
     * The logger
     */
    private final BukkitLogger m_logger;

    /**
     * The java plugin
     */
    private final JavaPlugin m_plugin;

    /**
     * The BH core
     */
    private IBlocksHubCore m_core;

    /**
     * The instance of the server
     */
    private final Server m_server;

    /**
     * The command manager
     */
    private final CommandManager m_commandManager;

    private static final ChatColor[] s_colors = new ChatColor[]{
        ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, //0-3
        ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY, //4-7
        ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, //8-b
        ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE, //c-f
        ChatColor.MAGIC, ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.UNDERLINE, //10-13
        ChatColor.ITALIC, null, null, null, //14-17
        null, null, null, null, //18-1b
        null, null, null, null, //1c-1f
        ChatColor.RESET //20
    };

    /**
     * The plugin version
     */
    private final String m_version;

    public BukkitPlatform(JavaPlugin plugin) {
        PluginDescriptionFile description = plugin.getDescription();

        m_version = description.getVersion();

        m_plugin = plugin;
        m_logger = new BukkitLogger(plugin);
        m_server = m_plugin.getServer();
        m_commandManager = new CommandManager(m_server, description.getName(), this);

        Colors.setPlatform(this);
    }

    /**
     * Initialize the platform
     *
     * @param core
     */
    @Override
    public void initialize(IBlocksHubCore core) {
        m_core = core;
    }

    @Override
    public void onEnable() {
        m_logger.onEnable();
    }

    @Override
    public void onDisable() {
        m_logger.onDisable();
    }

    @Override
    public String getVersion() {
        return m_version;
    }

    @Override
    public String getName() {
        return PLATFORM_NAME;
    }

    @Override
    public String getColorCode(Colors color) {
        if (color == null) {
            return "";
        }

        int code = color.getColorId();
        if (code < 0 || code >= s_colors.length) {
            return "";
        }

        return s_colors[code].toString();
    }

    @Override
    public IPlayer getPlayer(String name) {
        Player p = m_server.getPlayer(name);
        return p == null ? ConsolePlayer.getInstance() : new BukkitPlayer(p);
    }

    @Override
    public IPlayer getPlayer(UUID uuid) {
        Player p = m_server.getPlayer(uuid);
        return p == null ? ConsolePlayer.getInstance() : new BukkitPlayer(p);
    }

    @Override
    public IWorld getWorld(String name) {
        World world = m_server.getWorld(name);
        if (world == null) {
            return null;
        }

        return new BukkitWorld(world);
    }

    @Override
    public IWorld getWorld(UUID uuid) {
        World world = m_server.getWorld(uuid);
        if (world == null) {
            return null;
        }

        return new BukkitWorld(world);
    }

    @Override
    public ILogger getLogger() {
        return m_logger;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {
        Player player = cs instanceof Player ? (Player) cs : null;

        IPlayer commandSender = player == null ? ConsolePlayer.getInstance() : new BukkitPlayer(player);

        return m_core.onCommand(commandSender, command.getName(), args);
    }

    @Override
    public ICommandManager getCommandManager() {
        return m_commandManager;
    }

    @Override
    public IConfiguration getConfiguration() {
        m_plugin.saveDefaultConfig();

        Configuration configuration = m_plugin.getConfig();
        if (configuration == null) {
            return null;
        }

        configuration.setDefaults(new MemoryConfiguration());

        return new BukkitConfiguration(m_plugin, configuration);
    }

    @Override
    public void reloadConfig() {
        m_plugin.reloadConfig();
    }

    /**
     * Get plugin
     *
     * @param pluginName
     * @return
     */
    private Plugin getTypedPlugin(String pluginName) {
        if (pluginName == null) {
            return null;
        }

        PluginManager pm = m_plugin.getServer().getPluginManager();

        return pm.getPlugin(pluginName);
    }

    @Override
    public Object getPlugin(String pluginName) {
        return getTypedPlugin(pluginName);
    }

    @Override
    public String getPluginsDir() {
        return "/plugins/bukkit";
    }

    @Override
    public <T> T getPlatformLocation(IWorld world, double x, double y, double z, Class<T> locationType) {
        World w;
        if (world instanceof BukkitWorld) {
            w = ((BukkitWorld) world).getWorld();
        } else {
            w = m_server.getWorld(world.getUuid());
        }

        return (T) new Location(w, x, y, z);
    }

    @Override
    public <T> T getPlatformPlayer(IPlayer player, Class<T> type) {
        if (player instanceof LazyPlayer) {
            player = ((LazyPlayer)player).getResolved();
        }
        
        if (player instanceof BukkitPlayer) {
            return (T) ((BukkitPlayer)player).getPlayer();
        }
        
        return (T) m_server.getPlayer(player.getUUID());
        
    }

    @Override
    public <T> T getPlatformWorld(IWorld world, Class<T> type) {
        if (world instanceof BukkitWorld) {
            return (T) ((BukkitWorld) world).getWorld();
        }
        
        return (T) m_server.getWorld(world.getUuid());
    }
}
