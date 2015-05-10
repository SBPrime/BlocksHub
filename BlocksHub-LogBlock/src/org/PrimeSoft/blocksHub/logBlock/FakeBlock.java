/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.PrimeSoft.blocksHub.logBlock;

import java.util.Collection;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author SBPrime
 */
public class FakeBlock implements Block {
    private final byte m_data;
    private final Material m_type;
    private final Block m_parent;
    
    public FakeBlock(Block parent, byte data, Material type) {
        m_parent = parent;
        m_type = type;
        m_data = data;
    }
    
    @Override
    public byte getData() {
        return m_data;
    }

    @Override
    public Block getRelative(int i, int i1, int i2) {
        return m_parent.getRelative(i, i1, i2);
    }

    @Override
    public Block getRelative(BlockFace bf) {
        return m_parent.getRelative(bf);
    }

    @Override
    public Block getRelative(BlockFace bf, int i) {
        return m_parent.getRelative(bf,i);
    }

    @Override
    public Material getType() {
        return m_type;
    }

    @Override
    public int getTypeId() {
        return m_type.getId();
    }

    @Override
    public byte getLightLevel() {
        return m_parent.getLightLevel();
    }

    @Override
    public byte getLightFromSky() {
        return m_parent.getLightFromSky();
    }

    @Override
    public byte getLightFromBlocks() {
        return m_parent.getLightFromBlocks();
    }

    @Override
    public World getWorld() {
        return m_parent.getWorld();
    }

    @Override
    public int getX() {
        return m_parent.getX();
    }

    @Override
    public int getY() {
        return m_parent.getY();
    }

    @Override
    public int getZ() {
        return m_parent.getZ();
    }

    @Override
    public Location getLocation() {
        return m_parent.getLocation();
    }

    @Override
    public Location getLocation(Location lctn) {
        return m_parent.getLocation(lctn);
    }

    @Override
    public Chunk getChunk() {
        return m_parent.getChunk();
    }

    @Override
    public void setData(byte b) {
        
    }

    @Override
    public void setData(byte b, boolean bln) {
    }

    @Override
    public void setType(Material mtrl) {
    }

    @Override
    public void setType(Material mtrl, boolean bln) {
    }

    @Override
    public boolean setTypeId(int i) {
        return false;
    }

    @Override
    public boolean setTypeId(int i, boolean bln) {
        return false;
    }

    @Override
    public boolean setTypeIdAndData(int i, byte b, boolean bln) {
        return false;
    }

    @Override
    public BlockFace getFace(Block block) {
        return m_parent.getFace(block);
    }

    @Override
    public BlockState getState() {
        return new FakeBlockState(this, m_parent.getState());
    }

    @Override
    public Biome getBiome() {
        return m_parent.getBiome();
    }

    @Override
    public void setBiome(Biome biome) {
    }

    @Override
    public boolean isBlockPowered() {
        return m_parent.isBlockPowered();
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return m_parent.isBlockIndirectlyPowered();
    }

    @Override
    public boolean isBlockFacePowered(BlockFace bf) {
        return m_parent.isBlockFacePowered(bf);
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace bf) {
        return m_parent.isBlockFaceIndirectlyPowered(bf);
    }

    @Override
    public int getBlockPower(BlockFace bf) {
        return m_parent.getBlockPower(bf);
    }

    @Override
    public int getBlockPower() {
        return m_parent.getBlockPower();
    }

    @Override
    public boolean isEmpty() {
        return m_parent.isEmpty();
    }

    @Override
    public boolean isLiquid() {
        return m_parent.isLiquid();
    }

    @Override
    public double getTemperature() {
        return m_parent.getTemperature();
    }

    @Override
    public double getHumidity() {
        return m_parent.getHumidity();
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return m_parent.getPistonMoveReaction();
    }

    @Override
    public boolean breakNaturally() {
        return false;
    }

    @Override
    public boolean breakNaturally(ItemStack is) {
        return false;
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return m_parent.getDrops();
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack is) {
        return m_parent.getDrops(is);
    }

    @Override
    public void setMetadata(String string, MetadataValue mv) {        
    }

    @Override
    public List<MetadataValue> getMetadata(String string) {
        return m_parent.getMetadata(string);
    }

    @Override
    public boolean hasMetadata(String string) {
        return m_parent.hasMetadata(string);
    }

    @Override
    public void removeMetadata(String string, Plugin plugin) {
        return;
    }
    
}
