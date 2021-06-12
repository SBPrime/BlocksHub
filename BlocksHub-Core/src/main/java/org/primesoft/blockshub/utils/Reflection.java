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
package org.primesoft.blockshub.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.primesoft.blockshub.utils.LoggerProvider.log;

/**
 * Reflection GET and SET operations.
 *
 * @author SBPrime
 */
public class Reflection {
    public static <T> T create(Class<T> resultClass,
            Constructor<?> ctor, String message, Object... args) {
        try {
            return resultClass.cast(ctor.newInstance(args));
        } catch (InstantiationException ex) {
            log(String.format("%1$s: unsupported version.", message));
        } catch (InvocationTargetException ex) {
            log(String.format("%1$s: unsupported version.", message));
        } catch (IllegalArgumentException ex) {
            log(String.format("%1$s: unsupported version.", message));
        } catch (IllegalAccessException ex) {
            log(String.format("%1$s: security exception.", message));
        } catch (SecurityException ex) {
            log(String.format("%1$s: security exception.", message));
        } catch (ClassCastException ex) {
            log(String.format("%1$s: unsupported version, unable to cast result.", message));
        }

        return null;
    }

    public static <T> T get(Object instance, Class<T> fieldClass,
            String fieldName, String message) {
        return get(instance.getClass(), fieldClass, instance, fieldName, message);
    }

    public static <T> T get(final Class<?> sourceClass,
                            final Class<T> fieldClass,
                            final Object instance,
                            final String fieldName,
                            final String message) {

        try {
            final Field field = sourceClass.getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            return fieldClass.cast(field.get(instance));
        } catch (IllegalArgumentException ex) {
            log(String.format("%1$s: unsupported version.", message));
        } catch (IllegalAccessException ex) {
            log(String.format("%1$s: security exception.", message));
        } catch (NoSuchFieldException ex) {
            log(String.format("%1$s: unsupported version, field %2$s not found.", message, fieldName));
        } catch (SecurityException ex) {
            log(String.format("%1$s: security exception.", message));
        } catch (ClassCastException ex) {
            log(String.format("%1$s: unsupported version, unable to cast result.", message));
        }

        return null;
    }

    public static Constructor<?> findConstructor(Class<?> c, String message, Class<?>... paramTypes) {
        try {
            return c.getDeclaredConstructor(paramTypes);
        } catch (NoSuchMethodException ex) {
            log(String.format("%1$s: unsupported version, constructor not found.", message));
        } catch (SecurityException ex) {
            log(String.format("%1$s: security exception.", message));
        }

        return null;
    }
}
