package org.PrimeSoft.blocksHub.accessControl.GriefPrevention;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import me.ryanhamshire.GriefPrevention.BlockEventHandler;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

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
/**
 *
 * @author SBPrime
 */
public class GriefPrevention8x extends GriefPreventionBase {

    @Override
    public boolean Initialize(GriefPrevention hook) {
        try {
            if (hook == null) {
                return false;
            }
            
            Class<?> sourceClass = hook.getClass();
            Field field = sourceClass.getDeclaredField("dataStore");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");

            boolean accessible = modifiersField.isAccessible();
            if (!accessible) {
                modifiersField.setAccessible(true);
            }
            try {
                m_listener = (BlockEventHandler) BlockEventHandler.class.getConstructor(DataStore.class)
                        .newInstance(field.get(hook));
            } finally {
                if (!accessible) {
                    modifiersField.setAccessible(false);
                }
            }

            return m_listener != null;
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return false;
        } catch (SecurityException ex) {
            ex.printStackTrace();
            return false;
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            return false;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            return false;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return false;
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            return false;
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
