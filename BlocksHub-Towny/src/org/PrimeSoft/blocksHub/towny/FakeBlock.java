/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.PrimeSoft.blocksHub.towny;

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
 * @author SBPrimee
 */
public class FakeBlock implements Block {

    private final Location m_location;

    public FakeBlock(Location l) {
        m_location = l;
    }

    @Override
    public byte getData() {
        return 0;
    }

    @Override
    public Block getRelative(int i, int i1, int i2) {
        return new FakeBlock(m_location.add(i, i1, i2));
    }

    @Override
    public Block getRelative(BlockFace bf) {
        return getRelative(bf, 1);
    }

    @Override
    public Block getRelative(BlockFace bf, int i) {
        int x = 0;
        int y = 0;
        int z = 0;
        switch (bf) {
            case DOWN:
                y = -i;
                break;
            case EAST:
                x = i;
                break;
            case NORTH:
                y = -i;
                break;
            case SOUTH:
                y = i;
                break;
            case UP:
                y = i;
                break;
            case WEST:
                x = -i;
                break;
        }
        return getRelative(x, y, z);
    }

    @Override
    public Material getType() {
        return Material.STONE;
    }

    @Override
    public int getTypeId() {
        return Material.STONE.getId();
    }

    @Override
    public byte getLightLevel() {
        return 0;
    }

    @Override
    public byte getLightFromSky() {
        return 0;
    }

    @Override
    public byte getLightFromBlocks() {
        return 0;
    }

    @Override
    public World getWorld() {
        return m_location.getWorld();
    }

    @Override
    public int getX() {
        return m_location.getBlockX();
    }

    @Override
    public int getY() {
        return m_location.getBlockY();
    }

    @Override
    public int getZ() {
        return m_location.getBlockZ();
    }

    @Override
    public Location getLocation() {
        return m_location;
    }

    @Override
    public Location getLocation(Location lctn) {
        return m_location;
    }

    @Override
    public Chunk getChunk() {
        return m_location.getChunk();
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
        return BlockFace.DOWN;
    }

    @Override
    public BlockState getState() {
        return null;
    }

    @Override
    public Biome getBiome() {
        return null;
    }

    @Override
    public void setBiome(Biome biome) {
    }

    @Override
    public boolean isBlockPowered() {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return false;
    }

    @Override
    public boolean isBlockFacePowered(BlockFace bf) {
        return false;
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace bf) {
        return false;
    }

    @Override
    public int getBlockPower(BlockFace bf) {
        return 0;
    }

    @Override
    public int getBlockPower() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isLiquid() {
        return false;
    }

    @Override
    public double getTemperature() {
        return 0;
    }

    @Override
    public double getHumidity() {
        return 0;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.MOVE;
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
        return null;
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack is) {
        return null;
    }

    @Override
    public void setMetadata(String string, MetadataValue mv) {
    }

    @Override
    public List<MetadataValue> getMetadata(String string) {
        return null;
    }

    @Override
    public boolean hasMetadata(String string) {
        return false;
    }

    @Override
    public void removeMetadata(String string, Plugin plugin) {
    }

}
