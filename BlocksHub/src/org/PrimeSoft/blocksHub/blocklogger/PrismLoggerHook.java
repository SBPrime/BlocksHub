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
package org.PrimeSoft.blocksHub.blocklogger;

import java.lang.reflect.Method;

/**
 *
 * @author SBPrime
 */
public class PrismLoggerHook {
    private final String m_version;
    
    private final Class<?> m_cl;

    private final Class<?>[] m_methodParams;

    private final String m_method;

    public final String getVersion() {
        return m_version;
    }

    public PrismLoggerHook(Class<?> cl, String version, String method,
                           Class<?>[] methodParams) {
        m_cl = cl;
        m_version = version;
        m_method = method;
        m_methodParams = methodParams;
    }

    public Method getMethod()
            throws SecurityException {        
        try {
            return m_cl.getMethod(m_method, m_methodParams);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
}