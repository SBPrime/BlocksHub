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
package org.PrimeSoft.blocksHub;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

/**
 *
 * @author SBPrime
 */
public class SilentPlayer /*extends CraftPlayer*/ implements Player {
    private final Player m_parent;
    
    public SilentPlayer(Player parent){
        //super(null, null);
        m_parent = parent;
    }    

    @Override
    public void abandonConversation(Conversation conversation) {
        m_parent.abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation,
                                    ConversationAbandonedEvent details) {
        m_parent.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(String input) {
        m_parent.acceptConversationInput(input);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return m_parent.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return m_parent.addAttachment(plugin, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name,
                                              boolean value) {
        return m_parent.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name,
                                              boolean value, int ticks) {
        return m_parent.addAttachment(plugin, name, value, ticks);
    }


    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        return m_parent.addPotionEffect(effect);
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        return m_parent.addPotionEffect(effect, force);
    }

    @Override
    public boolean addPotionEffects(
                                    Collection<PotionEffect> effects) {
        return m_parent.addPotionEffects(effects);
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        m_parent.awardAchievement(achievement);
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return m_parent.beginConversation(conversation);
    }

    @Override
    public boolean canSee(Player player) {
        return m_parent.canSee(player);
    }

    @Override
    public void chat(String msg) {
        //Silent player ;)
        //m_parent.chat(msg);
    }

    @Override
    public void closeInventory() {
        m_parent.closeInventory();
    }

    @Override
    public void damage(double d) {
        m_parent.damage(d);
    }

    @Override
    public void damage(double d, Entity entity) {
        m_parent.damage(d, entity);
    }

    @Override
    public boolean eject() {
        return m_parent.eject();
    }

    @Override
    public boolean equals(Object obj) {
        return m_parent.equals(obj);
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return m_parent.getActivePotionEffects();
    }

    @Override
    public InetSocketAddress getAddress() {
        return m_parent.getAddress();
    }

    @Override
    public boolean getAllowFlight() {
        return m_parent.getAllowFlight();
    }

    @Override
    public Location getBedSpawnLocation() {
        return m_parent.getBedSpawnLocation();
    }

    @Override
    public boolean getCanPickupItems() {
        return m_parent.getCanPickupItems();
    }

    @Override
    public Location getCompassTarget() {
        return m_parent.getCompassTarget();
    }

    @Override
    public String getDisplayName() {
        return m_parent.getDisplayName();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return m_parent.getEffectivePermissions();
    }

    @Override
    public Inventory getEnderChest() {
        return m_parent.getEnderChest();
    }

    @Override
    public int getEntityId() {
        return m_parent.getEntityId();
    }

    @Override
    public EntityEquipment getEquipment() {
        return m_parent.getEquipment();
    }

    @Override
    public float getExhaustion() {
        return m_parent.getExhaustion();
    }

    @Override
    public float getExp() {
        return m_parent.getExp();
    }

    @Override
    public int getExpToLevel() {
        return m_parent.getExpToLevel();
    }

    @Override
    public double getEyeHeight() {
        return m_parent.getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignoreSneaking) {
        return m_parent.getEyeHeight(ignoreSneaking);
    }

    @Override
    public Location getEyeLocation() {
        return m_parent.getEyeLocation();
    }

    @Override
    public float getFallDistance() {
        return m_parent.getFallDistance();
    }

    @Override
    public int getFireTicks() {
        return m_parent.getFireTicks();
    }

    @Override
    public long getFirstPlayed() {
        return m_parent.getFirstPlayed();
    }

    @Override
    public float getFlySpeed() {
        return m_parent.getFlySpeed();
    }

    @Override
    public int getFoodLevel() {
        return m_parent.getFoodLevel();
    }

    @Override
    public GameMode getGameMode() {
        return m_parent.getGameMode();
    }

    @Override
    public double getHealth() {
        return m_parent.getHealth();
    }

    @Override
    public PlayerInventory getInventory() {
        return m_parent.getInventory();
    }

    @Override
    public ItemStack getItemInHand() {
        return m_parent.getItemInHand();
    }

    @Override
    public ItemStack getItemOnCursor() {
        return m_parent.getItemOnCursor();
    }

    @Override
    public Player getKiller() {
        return m_parent.getKiller();
    }

    @Override
    public double getLastDamage() {
        return m_parent.getLastDamage();
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return m_parent.getLastDamageCause();
    }

    @Override
    public long getLastPlayed() {
        return m_parent.getLastPlayed();
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(
                                              HashSet<Byte> transparent,
                                              int maxDistance) {
        return m_parent.getLastTwoTargetBlocks(transparent, maxDistance);
    }

    @Override
    public int getLevel() {
        return m_parent.getLevel();
    }

    @Override
    public List<Block> getLineOfSight(
                                      HashSet<Byte> transparent, int maxDistance) {
        return m_parent.getLineOfSight(transparent, maxDistance);
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return m_parent.getListeningPluginChannels();
    }

    @Override
    public Location getLocation() {
        return m_parent.getLocation();
    }

    @Override
    public Location getLocation(Location loc) {
        return m_parent.getLocation(loc);
    }

    public Player getM_parent() {
        return m_parent;
    }

    @Override
    public int getMaxFireTicks() {
        return m_parent.getMaxFireTicks();
    }

    @Override
    public double getMaxHealth() {
        return m_parent.getMaxHealth();
    }

    @Override
    public int getMaximumAir() {
        return m_parent.getMaximumAir();
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return m_parent.getMaximumNoDamageTicks();
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return m_parent.getMetadata(metadataKey);
    }


    @Override
    public String getName() {
        return m_parent.getName();
    }

    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        return m_parent.getNearbyEntities(x, y, z);
    }

    @Override
    public int getNoDamageTicks() {
        return m_parent.getNoDamageTicks();
    }

    @Override
    public InventoryView getOpenInventory() {
        return m_parent.getOpenInventory();
    }

    @Override
    public Entity getPassenger() {
        return m_parent.getPassenger();
    }

    @Override
    public Player getPlayer() {
        return m_parent.getPlayer();
    }

    @Override
    public String getPlayerListName() {
        return m_parent.getPlayerListName();
    }

    @Override
    public long getPlayerTime() {
        return m_parent.getPlayerTime();
    }

    @Override
    public long getPlayerTimeOffset() {
        return m_parent.getPlayerTimeOffset();
    }

    @Override
    public int getRemainingAir() {
        return m_parent.getRemainingAir();
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return m_parent.getRemoveWhenFarAway();
    }

    @Override
    public float getSaturation() {
        return m_parent.getSaturation();
    }

    @Override
    public Server getServer() {
        return m_parent.getServer();
    }

    @Override
    public int getSleepTicks() {
        return m_parent.getSleepTicks();
    }

    @Override
    public Block getTargetBlock(
                                HashSet<Byte> transparent, int maxDistance) {
        return m_parent.getTargetBlock(transparent, maxDistance);
    }

    @Override
    public int getTicksLived() {
        return m_parent.getTicksLived();
    }

    @Override
    public int getTotalExperience() {
        return m_parent.getTotalExperience();
    }

    @Override
    public EntityType getType() {
        return m_parent.getType();
    }

    @Override
    public UUID getUniqueId() {
        return m_parent.getUniqueId();
    }

    @Override
    public Entity getVehicle() {
        return m_parent.getVehicle();
    }

    @Override
    public Vector getVelocity() {
        return m_parent.getVelocity();
    }

    @Override
    public float getWalkSpeed() {
        return m_parent.getWalkSpeed();
    }

    @Override
    public World getWorld() {
        return m_parent.getWorld();
    }

    @Override
    public void giveExp(int exp) {
        m_parent.giveExp(exp);
    }

    @Override
    public void giveExpLevels(int levels) {
        m_parent.giveExpLevels(levels);
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        return m_parent.hasLineOfSight(other);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return m_parent.hasMetadata(metadataKey);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return m_parent.hasPermission(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return m_parent.hasPermission(name);
    }

    @Override
    public boolean hasPlayedBefore() {
        return m_parent.hasPlayedBefore();
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        return m_parent.hasPotionEffect(type);
    }

    @Override
    public int hashCode() {
        return m_parent.hashCode();
    }

    @Override
    public void hidePlayer(Player player) {
        m_parent.hidePlayer(player);
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        m_parent.incrementStatistic(statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        m_parent.incrementStatistic(statistic, material);
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        m_parent.incrementStatistic(statistic, amount);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material,
                                   int amount) {
        m_parent.incrementStatistic(statistic, material, amount);
    }

    @Override
    public boolean isBanned() {
        return m_parent.isBanned();
    }

    @Override
    public boolean isBlocking() {
        return m_parent.isBlocking();
    }

    @Override
    public boolean isConversing() {
        return m_parent.isConversing();
    }

    @Override
    public boolean isDead() {
        return m_parent.isDead();
    }

    @Override
    public boolean isEmpty() {
        return m_parent.isEmpty();
    }

    @Override
    public boolean isFlying() {
        return m_parent.isFlying();
    }

    @Override
    public boolean isInsideVehicle() {
        return m_parent.isInsideVehicle();
    }

    @Override
    public boolean isOnline() {
        return m_parent.isOnline();
    }

    @Override
    public boolean isOp() {
        return m_parent.isOp();
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return m_parent.isPermissionSet(perm);
    }

    @Override
    public boolean isPermissionSet(String name) {
        return m_parent.isPermissionSet(name);
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return m_parent.isPlayerTimeRelative();
    }

    @Override
    public boolean isSleeping() {
        return m_parent.isSleeping();
    }

    @Override
    public boolean isSleepingIgnored() {
        return m_parent.isSleepingIgnored();
    }

    @Override
    public boolean isSneaking() {
        return m_parent.isSneaking();
    }

    @Override
    public boolean isSprinting() {
        return m_parent.isSprinting();
    }

    @Override
    public boolean isValid() {
        return m_parent.isValid();
    }

    @Override
    public boolean isWhitelisted() {
        return m_parent.isWhitelisted();
    }

    @Override
    public void kickPlayer(String message) {
        m_parent.kickPlayer(message);
    }

    @Override
    public <T extends Projectile> T launchProjectile(
                                                     Class<? extends T> projectile) {
        return m_parent.launchProjectile(projectile);
    }

    @Override
    public boolean leaveVehicle() {
        return m_parent.leaveVehicle();
    }

    @Override
    public void loadData() {
        m_parent.loadData();
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        return m_parent.openEnchanting(location, force);
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        return m_parent.openInventory(inventory);
    }

    @Override
    public void openInventory(InventoryView inventory) {
        m_parent.openInventory(inventory);
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        return m_parent.openWorkbench(location, force);
    }

    @Override
    public boolean performCommand(String command) {
        return m_parent.performCommand(command);
    }

    @Override
    public void playEffect(EntityEffect type) {
        m_parent.playEffect(type);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        m_parent.playEffect(loc, effect, data);
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        m_parent.playEffect(loc, effect, data);
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        m_parent.playNote(loc, instrument, note);
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        m_parent.playNote(loc, instrument, note);
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        m_parent.playSound(loc, sound, volume, pitch);
    }

    @Override
    public void recalculatePermissions() {
        m_parent.recalculatePermissions();
    }

    @Override
    public void remove() {
        m_parent.remove();
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        m_parent.removeAttachment(attachment);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        m_parent.removeMetadata(metadataKey, owningPlugin);
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        m_parent.removePotionEffect(type);
    }

    @Override
    public void resetMaxHealth() {
        m_parent.resetMaxHealth();
    }

    @Override
    public void resetPlayerTime() {
        m_parent.resetPlayerTime();
    }

    @Override
    public void saveData() {
        m_parent.saveData();
    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        m_parent.sendBlockChange(loc, material, data);
    }

    @Override
    public void sendBlockChange(Location loc, int material, byte data) {
        m_parent.sendBlockChange(loc, material, data);
    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz,
                                   byte[] data) {
        return m_parent.sendChunkChange(loc, sx, sy, sz, data);
    }

    @Override
    public void sendMap(MapView map) {
        m_parent.sendMap(map);
    }

    @Override
    public void sendMessage(String message) {
        //Silent player ;)
        //m_parent.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        //Silent player ;)
        //m_parent.sendMessage(messages);
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        //Silent player ;)
        //m_parent.sendPluginMessage(source, channel, message);
    }

    @Override
    public void sendRawMessage(String message) {
        //Silent player ;)
        //m_parent.sendRawMessage(message);
    }


    @Override
    public Map<String, Object> serialize() {
        return m_parent.serialize();
    }

    @Override
    public void setAllowFlight(boolean value) {
        m_parent.setAllowFlight(value);
    }

    @Override
    public void setBanned(boolean value) {
        m_parent.setBanned(value);
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        m_parent.setBedSpawnLocation(location);
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean override) {
        m_parent.setBedSpawnLocation(location, override);
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        m_parent.setCanPickupItems(pickup);
    }

    @Override
    public void setCompassTarget(Location loc) {
        m_parent.setCompassTarget(loc);
    }

    @Override
    public void setDisplayName(String name) {
        m_parent.setDisplayName(name);
    }

    @Override
    public void setExhaustion(float value) {
        m_parent.setExhaustion(value);
    }

    @Override
    public void setExp(float exp) {
        m_parent.setExp(exp);
    }

    @Override
    public void setFallDistance(float distance) {
        m_parent.setFallDistance(distance);
    }

    @Override
    public void setFireTicks(int ticks) {
        m_parent.setFireTicks(ticks);
    }

    @Override
    public void setFlySpeed(float value) {
        m_parent.setFlySpeed(value);
    }


    @Override
    public void setFlying(boolean value) {
        m_parent.setFlying(value);
    }

    @Override
    public void setFoodLevel(int value) {
        m_parent.setFoodLevel(value);
    }

    @Override
    public void setGameMode(GameMode mode) {
        m_parent.setGameMode(mode);
    }

    @Override
    public void setHealth(double d) {
        m_parent.setHealth(d);
    }

    @Override
    public void setItemInHand(ItemStack item) {
        m_parent.setItemInHand(item);
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        m_parent.setItemOnCursor(item);
    }

    @Override
    public void setLastDamage(double d) {
        m_parent.setLastDamage(d);
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        m_parent.setLastDamageCause(event);
    }

    @Override
    public void setLevel(int level) {
        m_parent.setLevel(level);
    }

    @Override
    public void setMaxHealth(double d) {
        m_parent.setMaxHealth(d);
    }

    @Override
    public void setMaximumAir(int ticks) {
        m_parent.setMaximumAir(ticks);
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        m_parent.setMaximumNoDamageTicks(ticks);
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        m_parent.setMetadata(metadataKey, newMetadataValue);
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        m_parent.setNoDamageTicks(ticks);
    }

    @Override
    public void setOp(boolean value) {
        m_parent.setOp(value);
    }

    @Override
    public boolean setPassenger(Entity passenger) {
        return m_parent.setPassenger(passenger);
    }

    @Override
    public void setPlayerListName(String name) {
        m_parent.setPlayerListName(name);
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        m_parent.setPlayerTime(time, relative);
    }

    @Override
    public void setRemainingAir(int ticks) {
        m_parent.setRemainingAir(ticks);
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        m_parent.setRemoveWhenFarAway(remove);
    }

    @Override
    public void setSaturation(float value) {
        m_parent.setSaturation(value);
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        m_parent.setSleepingIgnored(isSleeping);
    }

    @Override
    public void setSneaking(boolean sneak) {
        m_parent.setSneaking(sneak);
    }

    @Override
    public void setSprinting(boolean sprinting) {
        m_parent.setSprinting(sprinting);
    }

    @Override
    public void setTexturePack(String url) {
        m_parent.setTexturePack(url);
    }

    @Override
    public void setTicksLived(int value) {
        m_parent.setTicksLived(value);
    }

    @Override
    public void setTotalExperience(int exp) {
        m_parent.setTotalExperience(exp);
    }

    @Override
    public void setVelocity(Vector vel) {
        m_parent.setVelocity(vel);
    }

    @Override
    public void setWalkSpeed(float value) {
        m_parent.setWalkSpeed(value);
    }

    @Override
    public void setWhitelisted(boolean value) {
        m_parent.setWhitelisted(value);
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return m_parent.setWindowProperty(prop, value);
    }

    @Override
    public Arrow shootArrow() {
        return m_parent.shootArrow();
    }

    @Override
    public void showPlayer(Player player) {
        m_parent.showPlayer(player);
    }

    @Override
    public boolean teleport(Entity destination) {
        return m_parent.teleport(destination);
    }

    @Override
    public boolean teleport(Location location) {
        return m_parent.teleport(location);
    }

    @Override
    public boolean teleport(Entity destination,
                            PlayerTeleportEvent.TeleportCause cause) {
        return m_parent.teleport(destination, cause);
    }

    @Override
    public boolean teleport(Location location,
                            PlayerTeleportEvent.TeleportCause cause) {
        return m_parent.teleport(location, cause);
    }

    @Override
    public Egg throwEgg() {
        return m_parent.throwEgg();
    }

    @Override
    public Snowball throwSnowball() {
        return m_parent.throwSnowball();
    }

    @Override
    public String toString() {
        return m_parent.toString();
    }

    @Override
    public void updateInventory() {
        m_parent.updateInventory();
    }

    /*@Override
    public void addChannel(String channel) {
        m_parent.addChannel(channel);
    }*/

    @Override
    public void decrementStatistic(Statistic statistic) {
        m_parent.decrementStatistic(statistic);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        m_parent.decrementStatistic(statistic, entityType);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {
        m_parent.decrementStatistic(statistic, material);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {
        m_parent.decrementStatistic(statistic, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        m_parent.decrementStatistic(statistic, entityType, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        m_parent.decrementStatistic(statistic, material, amount);
    }

    /*@Override
    public void disconnect(String reason) {
        m_parent.disconnect(reason);
    }*/
    

    @Override
    public String getCustomName() {
        return m_parent.getCustomName();
    }

    /*@Override
    public EntityPlayer getHandle() {
        return m_parent.getHandle();
    }*/

    @Override
    public double getHealthScale() {
        return m_parent.getHealthScale();
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return m_parent.getLeashHolder();
    }

    /*@Override
    public Vector getMomentum() {
        return m_parent.getMomentum();
    }*/

    @Override
    public WeatherType getPlayerWeather() {
        return m_parent.getPlayerWeather();
    }

    /*@Override
    public GameProfile getProfile() {
        return m_parent.getProfile();
    }

    @Override
    public float getScaledHealth() {
        return m_parent.getScaledHealth();
    }*/

    @Override
    public Scoreboard getScoreboard() {
        return m_parent.getScoreboard();
    }

    @Override
    public int getStatistic(Statistic statistic) {
        return m_parent.getStatistic(statistic);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        return m_parent.getStatistic(statistic, entityType);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        return m_parent.getStatistic(statistic, material);
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        return m_parent.hasAchievement(achievement);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        m_parent.incrementStatistic(statistic, entityType);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        m_parent.incrementStatistic(statistic, entityType, amount);
    }

    /*@Override
    public void injectScaledMaxHealth(Collection collection, boolean force) {
        m_parent.injectScaledMaxHealth(collection, force);
    }*/

    @Override
    public boolean isCustomNameVisible() {
        return m_parent.isCustomNameVisible();
    }

    @Override
    public boolean isHealthScaled() {
        return m_parent.isHealthScaled();
    }

    @Override
    public boolean isLeashed() {
        return m_parent.isLeashed();
    }

    @Override
    public boolean isOnGround() {
        return m_parent.isOnGround();
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        return m_parent.launchProjectile(projectile, velocity);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        m_parent.playSound(loc, sound, volume, pitch);
    }

    /*@Override
    public void readExtraData(NBTTagCompound nbttagcompound) {
        m_parent.readExtraData(nbttagcompound);
    }*/

    @Override
    public void removeAchievement(Achievement achievement) {
        m_parent.removeAchievement(achievement);
    }

    /*@Override
    public void removeChannel(String channel) {
        m_parent.removeChannel(channel);
    }

    @Override
    public void removeDisconnectingPlayer(Player player) {
        m_parent.removeDisconnectingPlayer(player);
    }*/

    @Override
    public void resetPlayerWeather() {
        m_parent.resetPlayerWeather();
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) {
        m_parent.sendSignChange(loc, lines);
    }

    /*@Override
    public void sendSupportedChannels() {
        m_parent.sendSupportedChannels();
    }*/

    @Override
    public void setCustomName(String name) {
        m_parent.setCustomName(name);
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        m_parent.setCustomNameVisible(flag);
    }

    /*@Override
    public void setExtraData(NBTTagCompound nbttagcompound) {
        m_parent.setExtraData(nbttagcompound);
    }

    @Override
    public void setFirstPlayed(long firstPlayed) {
        m_parent.setFirstPlayed(firstPlayed);
    }

    @Override
    public void setHandle(net.minecraft.server.v1_7_R3.Entity entity) {
        m_parent.setHandle(entity);
    }

    @Override
    public void setHandle(EntityHuman entity) {
        m_parent.setHandle(entity);
    }

    @Override
    public void setHandle(EntityLiving entity) {
        m_parent.setHandle(entity);
    }

    @Override
    public void setHandle(EntityPlayer entity) {
        m_parent.setHandle(entity);
    }*/

    @Override
    public void setHealthScale(double value) {
        m_parent.setHealthScale(value);
    }

    @Override
    public void setHealthScaled(boolean scale) {
        m_parent.setHealthScaled(scale);
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        return m_parent.setLeashHolder(holder);
    }

    /*@Override
    public void setMomentum(Vector value) {
        m_parent.setMomentum(value);
    }*/

    @Override
    public void setPlayerWeather(WeatherType type) {
        m_parent.setPlayerWeather(type);
    }

    /*@Override
    public void setRealHealth(double health) {
        m_parent.setRealHealth(health);
    }*/

    @Override
    public void setResourcePack(String url) {
        m_parent.setResourcePack(url);
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        m_parent.setScoreboard(scoreboard);
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) {
        m_parent.setStatistic(statistic, newValue);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        m_parent.setStatistic(statistic, entityType, newValue);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {
        m_parent.setStatistic(statistic, material, newValue);
    }

    /*@Override
    public void updateScaledHealth() {
        m_parent.updateScaledHealth();
    } */       


    @Override
    public int _INVALID_getLastDamage() {
        return (int)m_parent.getLastDamage();
    }

    @Override
    public void _INVALID_setLastDamage(int i) {
        m_parent.setLastDamage(i);
    }

    @Override
    public void _INVALID_damage(int i) {
        m_parent.damage(i);
    }

    @Override
    public void _INVALID_damage(int i, Entity entity) {
        m_parent.damage(i, entity);
    }

    @Override
    public int _INVALID_getHealth() {
        return (int)m_parent.getHealth();
    }

    @Override
    public void _INVALID_setHealth(int i) {
        m_parent.setHealth(i);
    }

    @Override
    public int _INVALID_getMaxHealth() {
        return (int)m_parent.getMaxHealth();
    }

    @Override
    public void _INVALID_setMaxHealth(int i) {
        m_parent.setMaxHealth(i);
    }
}
