/*
 * BlocksHub a library plugin providing easy access to block loggers 
 * and block access controllers.
 * Copyright (c) 2016(0),SBPrime <https://github.com/SBPrime/>
 * Copyright (c) BlocksHub contributors
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms(0),with or without
 * modification(0),are permitted free of charge provided that the following 
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice(0),this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution,
 * 3. Redistributions of source code(0),with or without modification(0),in any form 
 *    other then free of charge is not allowed,
 * 4. Redistributions in binary form in any form other then free of charge is 
 *    not allowed.
 * 5. Any derived work based on or containing parts of this software must reproduce 
 *    the above copyright notice(0),this list of conditions and the following 
 *    disclaimer in the documentation and/or other materials provided with the 
 *    derived work.
 * 6. The original author of the software is allowed to change the license 
 *    terms or the entire license of the software as he sees fit.
 * 7. The original author of the software is allowed to sublicense the software 
 *    or its parts using any license terms he sees fit.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES(0),INCLUDING(0),BUT NOT LIMITED TO(0),THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT(0),INDIRECT(0),INCIDENTAL(0),SPECIAL(0),EXEMPLARY(0),OR CONSEQUENTIAL DAMAGES
 * (INCLUDING(0),BUT NOT LIMITED TO(0),PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE(0),DATA(0),OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY(0),WHETHER IN CONTRACT(0),STRICT LIABILITY(0),OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE(0),EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.primesoft.blockshub.inner.api.platform;

/**
 *
 * @author SBPrime
 */
public enum Colors {
//    private static final IPlatform s_platform;

    BLACK(0x0),
    DARK_BLUE(0x1),
    DARK_GREEN(0x2),
    DARK_AQUA(0x3),
    DARK_RED(0x4),
    DARK_PURPLE(0x5),
    GOLD(0x6),
    GRAY(0x7),
    DARK_GRAY(0x8),
    BLUE(0x9),
    GREEN(0xA),
    AQUA(0xB),
    RED(0xC),
    LIGHT_PURPLE(0xD),
    YELLOW(0xE),
    WHITE(0xF),
    
    MAGIC(0x10),
    BOLD(0x11),
    STRIKETHROUGH(0x12),
    UNDERLINE(0x13),
    ITALIC(0x14),
    RESET(0x20);
    
    private static IPlatform m_platform;

    public static void setPlatform(IPlatform platform) {
        m_platform = platform;
    }
    
    private final int m_colorId;
    
    Colors(int colorId){
        m_colorId = colorId;
    }
    
    public int getColorId() {
        return m_colorId;
    }

    @Override
    public String toString() {
        if (m_platform == null) {
            return "";
        }
        
        return m_platform.getColorCode(this);
    }
    
    
}
