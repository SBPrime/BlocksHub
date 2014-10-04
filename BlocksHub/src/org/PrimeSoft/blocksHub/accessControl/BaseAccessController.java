/*
 * The MIT License
 *
 * Copyright 2014 SBPrime.
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
package org.PrimeSoft.blocksHub.accessControl;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author SBPrime
 * @param <T>
 */
public abstract class BaseAccessController<T extends Plugin> implements IAccessController {

    /**
     * AC name
     */
    private final String m_name;

    /**
     * Craftbukkit server
     */
    protected Server m_server;

    /**
     * Is world guard integration enabled
     */
    protected boolean m_isEnabled;

    /**
     * The hook class
     */
    protected final T m_hook;

    @Override
    public boolean isEnabled() {
        return m_isEnabled;
    }

    /**
     * Get access controller name
     *
     * @return
     */
    @Override
    public String getName() {
        return m_name;
    }

    protected void disable() {

    }

    protected BaseAccessController(JavaPlugin plugin, final String pluginName) {
        m_isEnabled = false;
        PluginDescriptionFile pd = null;
        T hook = null;
        try {
            Plugin cPlugin = plugin.getServer().getPluginManager().getPlugin(pluginName);

            if ((cPlugin != null) && instanceOfT(cPlugin.getClass())) {
                m_isEnabled = true;
                hook = (T) cPlugin;
                m_server = plugin.getServer();

                pd = hook.getDescription();
            }
        } catch (NoClassDefFoundError ex) {
            hook = null;
            m_isEnabled = false;
        }

        m_hook = hook;
        if (m_isEnabled) {
            m_isEnabled = postInit(pd);
        }        
        m_name = pd != null && m_isEnabled ? pd.getFullName() : "Disabled - " + pluginName;
    }

    protected boolean postInit(PluginDescriptionFile pd) {
        return true;
    }

    protected abstract boolean instanceOfT(Class<? extends Plugin> aClass);
}
