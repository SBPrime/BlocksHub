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
package org.PrimeSoft.blocksHub.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import org.PrimeSoft.blocksHub.BlocksHub;
import org.PrimeSoft.blocksHub.accessControl.AccessControllers;
import org.PrimeSoft.blocksHub.blocklogger.Loggers;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

/**
 * This class contains configuration
 *
 * @author SBPrime
 */
public class ConfigProvider {

    private static final int CONFIG_VERSION = 1;
    private static boolean m_isConfigUpdate = false;
    private static int m_configVersion;
    private static String[] m_loggers;
    private static String[] m_accessControlers;
    private final static HashSet<String> m_enabledWorlds = new HashSet<String>();

    public static String[] getLoggers() {
        return m_loggers;
    }

    public static String[] getAccessControlers() {
        return m_accessControlers;
    }

    public static int getConfigVersion() {
        return m_configVersion;
    }

    /**
     * Is the world being logged
     *
     * @return
     */
    public static boolean isLogging(String world) {
        if (world == null) {
            return false;
        }                
        
        if (m_enabledWorlds.contains(world.toLowerCase())) {
            return true;
        }
        
        for (String pattern : m_enabledWorlds) {
            if (Pattern.matches(pattern, world)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Is the configuration up to date
     *
     * @return
     */
    public static boolean isConfigUpdated() {
        return m_isConfigUpdate;
    }

    /**
     * Load configuration
     *
     * @param plugin parent plugin
     * @return true if config loaded
     */
    public static boolean load(BlocksHub plugin) {
        if (plugin == null) {
            return false;
        }

        plugin.saveDefaultConfig();

        Configuration config = plugin.getConfig();
        ConfigurationSection mainSection = config.getConfigurationSection("BlocksHub");
        if (mainSection == null) {
            return false;
        }
        m_configVersion = mainSection.getInt("version", 1);
        m_isConfigUpdate = m_configVersion == CONFIG_VERSION;

        parseLoggers(mainSection.getConfigurationSection("loggers"));
        parseAccessControlers(mainSection.getStringList("access"));

        if (m_accessControlers == null) {
            m_accessControlers = new String[0];
        }

        if (m_loggers == null) {
            m_loggers = new String[0];
        }

        return true;
    }

    private static void parseLoggers(ConfigurationSection subSection) {
        if (subSection == null) {
            return;
        }

        List<String> loggers = subSection.getStringList("enabled");
        List<String> worlds = subSection.getStringList("worlds");

        if (loggers == null) {
            loggers = new ArrayList<String>();
        }
        if (worlds == null) {
            worlds = new ArrayList<String>();
        }

        m_enabledWorlds.clear();
        for (String world : worlds) {
            world = world.toLowerCase();
            if (!m_enabledWorlds.contains(world)) {
                m_enabledWorlds.add(world);
            }
        }

        HashSet<String> filtered = new HashSet<String>();
        for (String name : loggers) {
            name = name.toLowerCase();
            if (!name.equalsIgnoreCase(Loggers.CORE_PROTECT)
                    && !name.equalsIgnoreCase(Loggers.HAWK_EYE)
                    && !name.equalsIgnoreCase(Loggers.LOG_BLOCK)
                    && !name.equalsIgnoreCase(Loggers.PRISM)) {
                BlocksHub.log("Unknown blocks logger: " + name);
            } else if (filtered.contains(name)) {
                BlocksHub.log("Duplicate logger entry: " + name);
            } else {
                filtered.add(name);
            }
        }

        m_loggers = filtered.toArray(new String[0]);
    }

    private static void parseAccessControlers(List<String> list) {
        if (list == null) {
            return;
        }

        HashSet<String> filtered = new HashSet<String>();
        for (String name : list) {
            name = name.toLowerCase();
            if (!name.equalsIgnoreCase(AccessControllers.WORLD_GUARD)) {
                BlocksHub.log("Unknown access controller: " + name);
            } else if (filtered.contains(name)) {
                BlocksHub.log("Duplicate access controller: " + name);
            } else {
                filtered.add(name);
            }

        }
        m_accessControlers = filtered.toArray(new String[0]);
    }
}