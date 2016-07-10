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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import static org.primesoft.blockshub.LoggerProvider.log;

/**
 *
 * @author SBPrime
 */
public class ResourceClassLoader extends ClassLoader {
    private final static int BUF_SIZE = 0x10000;

    /**
     * The list of known classes
     */
    private final Map<String, Class<?>> m_classHash;

    /**
     * Parrent class loader
     */
    private final ClassLoader m_parrent;

    /**
     * Create the class loader
     *
     * @param resourceName
     * @return
     */
    public static ResourceClassLoader create(String resourceName) {
        Class<?> cls = ResourceClassLoader.class;
        Map<String, byte[]> files;

        InputStream is = null;
        try {
            is = cls.getResourceAsStream(resourceName);
            if (is == null) {
                log(String.format("Resource %1$s not found.", resourceName));
                return null;
            }

            files = loadAllClasses(is);
            if (files.isEmpty()) {
                log(String.format("Resource %1$s contains no classes", resourceName));
                return null;
            }
            is.close();
        } catch (IOException ex) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException eex) {
                }
            }
            ExceptionHelper.printException(ex, String.format("Unable to load %1$s", resourceName));
            return null;
        }

        ClassLoader parrent = cls.getClassLoader();

        try {
            ResourceClassLoader result = new ResourceClassLoader(parrent);
            for (String fileName : files.keySet()) {
                byte[] data = files.get(fileName);
                result.createClass(fileName, data);
            }

            return result;
        } catch (Error ex) {
            ExceptionHelper.printException(ex, "Unable to cache classes.");
            return null;
        }
    }

    /**
     * Read input stream to byte array
     *
     * @param is
     * @return
     * @throws IOException
     */
    private static byte[] readFully(InputStream is) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[BUF_SIZE];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();

            return buffer.toByteArray();
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Read all jar entries to memory
     *
     * @param jarStream
     * @return
     */
    private static Map<String, byte[]> loadAllClasses(InputStream is) throws IOException {
        Map<String, byte[]> files = new LinkedHashMap<String, byte[]>();

        JarInputStream jarStream = new JarInputStream(is);

        ZipEntry entry;

        while ((entry = jarStream.getNextEntry()) != null) {
            String name = entry.getName();
            if (!name.endsWith(".class")) {
                continue;
            }

            byte[] data = readFully(jarStream);
            if (data == null || data.length == 0) {
                continue;
            }

            files.put(name, data);
        }

        return files;
    }

    private ResourceClassLoader(ClassLoader parrent) {
        super(parrent);

        m_parrent = parrent;
        m_classHash = new LinkedHashMap<String, Class<?>>();
    }

    /**
     * Create class and cache it (called by the static creator)
     *
     * @param name
     * @param data
     */
    private void createClass(String name, byte[] data) {
        String className = name.substring(0, name.length() - 6).replace('/', '.');
        m_classHash.put(className, super.defineClass(className, data, 0, data.length));
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (m_classHash.containsKey(name)) {
            return m_classHash.get(name);
        }

        return super.loadClass(name, resolve);
    }

    
    /**
     * Get main entry point
     * @return 
     */
    public Collection<Class<?>> getAllClasses() {
        return m_classHash.values();        
    }
}
