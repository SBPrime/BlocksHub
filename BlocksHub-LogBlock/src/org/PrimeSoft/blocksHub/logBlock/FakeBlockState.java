/*
 * BlocksHub a library plugin providing easy access to block loggers 
 * and block access controllers.
 * Copyright (c) 2013, SBPrime <https://github.com/SBPrime/>
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

package org.PrimeSoft.blocksHub.logBlock;

import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author prime
 */
public class FakeBlockState implements  BlockState {
    private final BlockState m_parent;
    private final FakeBlock m_fakeBlock;
    private final MaterialData m_materialData;
    
    public FakeBlockState(FakeBlock fakeBlock, BlockState blockState)
    {
        m_parent = blockState;
        m_fakeBlock = fakeBlock;
        m_materialData = new MaterialData(m_fakeBlock.getType(), m_fakeBlock.getData());
    }

    @Override
    public Block getBlock() {
        return m_fakeBlock;
    }

    @Override
    public MaterialData getData() {
        return m_materialData;
    }

    @Override
    public Material getType() {
        return m_fakeBlock.getType();
    }

    @Override
    public int getTypeId() {
        return m_fakeBlock.getTypeId();
    }

    @Override
    public byte getLightLevel() {
        return m_fakeBlock.getLightLevel();
    }

    @Override
    public World getWorld() {
        return m_fakeBlock.getWorld();
    }

    @Override
    public int getX() {
        return m_fakeBlock.getX();
    }

    @Override
    public int getY() {
        return m_fakeBlock.getY();
    }

    @Override
    public int getZ() {
        return m_fakeBlock.getZ();
    }

    @Override
    public Location getLocation() {
        return m_fakeBlock.getLocation();
    }

    @Override
    public Location getLocation(Location lctn) {
        return m_fakeBlock.getLocation(lctn);
    }

    @Override
    public Chunk getChunk() {
        return m_fakeBlock.getChunk();
    }

    @Override
    public void setData(MaterialData md) {
        
    }

    @Override
    public void setType(Material mtrl) {
    }

    @Override
    public boolean setTypeId(int i) {
        return false;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean update(boolean bln) {
        return false;
    }

    @Override
    public boolean update(boolean bln, boolean bln1) {
        return false;
    }

    @Override
    public byte getRawData() {
        return m_fakeBlock.getData();
    }

    @Override
    public void setRawData(byte b) {
        
    }

    @Override
    public boolean isPlaced() {
        return m_parent.isPlaced();
    }

    @Override
    public void setMetadata(String string, MetadataValue mv) {
    }

    @Override
    public List<MetadataValue> getMetadata(String string) {
        return m_fakeBlock.getMetadata(string);
    }

    @Override
    public boolean hasMetadata(String string) {
        return m_fakeBlock.hasMetadata(string);
    }

    @Override
    public void removeMetadata(String string, Plugin plugin) {
        
    }
}
