package com.zpedroo.voltzspawners.objects;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class PlacedSpawner {
    
    private final Location location;
    private final UUID ownerUUID;
    private BigInteger stack;
//    private final Map<Upgrade, Integer> upgrades;
    private final Spawner spawner;
    private SpawnerHologram hologram;
    private final Set<Entity> entities;
    private int spawnDelay;
    private boolean loaded;
    private boolean update;
    
    public PlacedSpawner(Location location, UUID ownerUUID, BigInteger stack, /*Map<Upgrade, Integer> upgrades,*/ Spawner spawner) {
        this.location = location;
        this.ownerUUID = ownerUUID;
        this.stack = stack;
//        this.upgrades = upgrades;
        this.spawner = spawner;
        this.entities = new HashSet<>(1);
        this.spawnDelay = spawner.getSpawnDelay();
        this.loaded = false;
        this.update = false;
        this.load();
    }

    /*
    public int getUpgradeLevel(Upgrade upgrade) {
        return upgrades.getOrDefault(upgrade, 1);
    }
     */

    public boolean isLoaded() {
        return loaded;
    }
    
    public boolean isQueueUpdate() {
        return update;
    }

    public boolean hasReachStackLimit() {
        return spawner.getMaximumStack().signum() > 0 && stack.compareTo(spawner.getMaximumStack()) >= 0;
    }
    
    public boolean canInteract(Player player) {
        return player.getUniqueId().equals(ownerUUID) || player.hasPermission("spawners.admin");
    }
    
    public void delete() {
        DataManager.getInstance().getCache().getDeletedSpawners().add(location);
        DataManager.getInstance().getCache().getPlacedSpawners().remove(location);

        removeEntities();
        location.getBlock().setType(Material.AIR);
        if (hologram != null) hologram.removeHologram();
    }
    
    public String replacePlaceholders(String text) {
        if (text == null || text.isEmpty()) return "";

        return StringUtils.replaceEach(text, new String[]{
                "{owner}",
                "{stack}",
                "{max_stack}",
                "{type}"
        }, new String[]{
                Bukkit.getOfflinePlayer(ownerUUID).getName(),
                NumberFormatter.getInstance().format(stack),
                NumberFormatter.getInstance().format(spawner.getMaximumStack()),
                spawner.getTypeTranslated()
        });
    }
    
    public void updateDelay() {
        this.spawnDelay = spawner.getSpawnDelay();
    }
    
    public void addStack(BigInteger amount) {
        this.setStack(stack.add(amount));
    }
    
    public void removeStack(BigInteger amount) {
        this.setStack(stack.subtract(amount));
    }
    
    public void setStack(BigInteger amount) {
        this.stack = amount;
        this.update = true;
        if (stack.signum() <= 0) {
            VoltzSpawners.get().getServer().getScheduler().runTaskLater(VoltzSpawners.get(), this::delete, 0L);
            return;
        }

        hologram.updateHologram();
    }
    
    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    
    public void removeEntities() {
        for (Entity entity : entities) {
            entity.remove();
        }

        entities.clear();
    }
    
    public void load() {
        this.loaded = true;
        if (hologram == null) {
            hologram = new SpawnerHologram(this);
        }

        new BukkitRunnable() {
            public void run() {
                Block block = location.getBlock();
                block.setType(Material.MOB_SPAWNER);

                CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
                creatureSpawner.setSpawnedType(spawner.getEntityType());
                creatureSpawner.setDelay(Integer.MAX_VALUE);
            }
        }.runTaskLater(VoltzSpawners.get(), 0L);
    }
    
    public void unload() {
        this.loaded = false;
        if (hologram != null) {
            hologram.removeHologram();
            hologram = null;
        }
    }
    
    public void cache() {
        DataManager.getInstance().getCache().getPlacedSpawners().put(location, this);
    }
}