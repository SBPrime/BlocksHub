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
package org.PrimeSoft.blocksHub.blocklogger;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionFactory;
import me.botsko.prism.actionlibs.RecordingQueue;
import me.botsko.prism.actions.Handler;
import org.PrimeSoft.blocksHub.BlocksHub;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author SBPrime
 */
public class PrismLogger implements IBlockLogger {

    /**
     * Plugin name
     */
    private final String m_name;

    /**
     * Is the logger enabled
     */
    private boolean m_isEnabled;

    /**
     * Get instance of the prism plugin
     *
     * @param plugin
     * @return
     */
    public static Prism getPrism(JavaPlugin plugin) {
        try {
            Plugin cPlugin = plugin.getServer().getPluginManager().getPlugin("Prism");

            if ((cPlugin == null) || (!(cPlugin instanceof Prism))) {
                return null;
            }

            return (Prism) cPlugin;
        } catch (NoClassDefFoundError ex) {
            return null;
        }
    }

    public PrismLogger(JavaPlugin plugin) {
        Prism prism = getPrism(plugin);
        PluginDescriptionFile pd = null;
        if (prism == null) {
            m_isEnabled = false;
        } else {
            m_isEnabled = true;
            pd = prism.getDescription();
        }

        m_name = pd != null ? pd.getFullName() : "Disabled - PrismLogger";

    }

    @Override
    public void logBlock(Location location, String player, World world,
            int oldBlockType, byte oldBlockData, int newBlockType,
            byte newBlockData) {
        if (!m_isEnabled) {
            return;
        }

        Location l = new Location(world, location.getBlockX(), location.getBlockY(), location.getBlockZ());
        /*
         * Prism version 1.x
         * Handler action = ActionFactory.create("world-edit", l,
         *               oldBlockType, oldBlockData,
         *               newBlockType, newBlockData,
         *               player);
         *
         * Prism.actionsRecorder.addToQueue(action);
         */
        RecordingQueue.addToQueue(ActionFactory.createBlockChange("world-edit", l, oldBlockType, oldBlockData, newBlockType, newBlockData, player));
    }

    @Override
    public boolean isEnabled() {
        return m_isEnabled;
    }

    @Override
    public String getName() {
        return m_name;
    }
}
