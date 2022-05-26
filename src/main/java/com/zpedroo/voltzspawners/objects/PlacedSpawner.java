package com.zpedroo.voltzspawners.objects;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.DataManager;
<<<<<<< HEAD
=======
import com.zpedroo.voltzspawners.utils.config.Messages;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
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
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import java.util.Set;
import java.util.UUID;

public class PlacedSpawner {

<<<<<<< HEAD
    private final Location location;
    private final UUID ownerUUID;
    private BigInteger stack;
    private final Spawner spawner;
    private SpawnerHologram hologram;
    private final Set<Entity> entities;
    private int spawnDelay;
    private boolean loaded;
    private boolean update;

    public PlacedSpawner(Location location, UUID ownerUUID, BigInteger stack, Spawner spawner) {
=======
    private Location location;
    private UUID ownerUUID;
    private BigInteger stack;
    private Spawner spawner;
    private SpawnerHologram hologram;
    private List<Manager> managers;
    private Set<Entity> entities;
    private boolean publicSpawner;
    private boolean loaded;
    private boolean update;
    private int spawnDelay;

    public PlacedSpawner(Location location, UUID ownerUUID, BigInteger stack, Spawner spawner, List<Manager> managers, boolean publicSpawner) {
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        this.location = location;
        this.ownerUUID = ownerUUID;
        this.stack = stack;
        this.spawner = spawner;
<<<<<<< HEAD
        this.entities = new HashSet<>(1);
        this.spawnDelay = spawner.getSpawnDelay();
        this.loaded = false;
        this.update = false;
=======
        this.managers = managers;
        this.entities = new HashSet<>(1);
        this.publicSpawner = publicSpawner;
        this.loaded = false;
        this.update = false;
        this.spawnDelay = spawner.getSpawnDelay();
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    public Location getLocation() {
        return location;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public BigInteger getStack() {
        return stack;
    }

    public Spawner getSpawner() {
        return spawner;
    }

<<<<<<< HEAD
=======
    public List<Manager> getManagers() {
        return managers;
    }

    public Manager getManager(UUID uuid) {
        for (Manager manager : managers) {
            if (!manager.getUUID().equals(uuid)) continue;

            return manager;
        }

        return null;
    }

    public boolean isPublic() {
        return publicSpawner;
    }

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    public boolean isLoaded() {
        return loaded;
    }

<<<<<<< HEAD
    public boolean isUpdate() {
=======
    public boolean isQueueUpdate() {
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        return update;
    }

    public boolean hasReachStackLimit() {
        if (spawner.getMaximumStack().signum() <= 0) return false;

        return stack.compareTo(spawner.getMaximumStack()) >= 0;
    }

    public boolean canInteract(Player player) {
<<<<<<< HEAD
        return player.getUniqueId().equals(ownerUUID) || player.hasPermission("spawners.admin");
=======
        if (player.getUniqueId().equals(getOwnerUUID())) return true;
        if (player.hasPermission("spawners.admin")) return true;

        Manager manager = getManager(player.getUniqueId());
        return manager != null;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    public int getSpawnDelay() {
        return spawnDelay;
    }

    public SpawnerHologram getHologram() {
        return hologram;
    }

    public void delete() {
        DataManager.getInstance().getCache().getDeletedSpawners().add(location);
        DataManager.getInstance().getCache().getPlacedSpawners().remove(location);

        this.removeEntities();
        this.location.getBlock().setType(Material.AIR);
<<<<<<< HEAD
        if (hologram != null) this.hologram.removeHologram();
=======
        this.hologram.removeHologramAndItem();
    }

    public void setPublic(boolean publicSpawner) {
        this.publicSpawner = publicSpawner;
        this.update = true;
        this.hologram.updateHologramAndItem();
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    public String replace(String text) {
        if (text == null || text.isEmpty()) return "";

        return StringUtils.replaceEach(text, new String[] {
                "{owner}",
                "{stack}",
                "{max_stack}",
<<<<<<< HEAD
                "{type}"
=======
                "{type}",
                "{privacy_status}"
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        }, new String[] {
                Bukkit.getOfflinePlayer(ownerUUID).getName(),
                NumberFormatter.getInstance().format(stack),
                NumberFormatter.getInstance().format(spawner.getMaximumStack()),
<<<<<<< HEAD
                spawner.getTypeTranslated()
=======
                spawner.getTypeTranslated(),
                publicSpawner ? Messages.TRUE : Messages.FALSE
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        });
    }

    public void updateDelay() {
        this.spawnDelay = spawner.getSpawnDelay();
    }

    public void setSpawnDelay(int spawnDelay) {
        this.spawnDelay = spawnDelay;
    }

    public void setUpdate(boolean update) {
        this.update = update;
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
            VoltzSpawners.get().getServer().getScheduler().runTaskLater(VoltzSpawners.get(), this::delete, 0L); // fix async block remove
            return;
        }

<<<<<<< HEAD
        this.hologram.updateHologram();
=======
        this.hologram.updateHologramAndItem();
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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
<<<<<<< HEAD
        if (hologram == null) this.hologram = new SpawnerHologram(this);
=======

        this.hologram = new SpawnerHologram(this);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

        new BukkitRunnable() {
            @Override
            public void run() {
                Block block = location.getWorld().getBlockAt(location);
                block.setType(Material.MOB_SPAWNER);
                CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
                creatureSpawner.setSpawnedType(spawner.getEntityType());
                creatureSpawner.setDelay(Integer.MAX_VALUE);
            }
        }.runTaskLater(VoltzSpawners.get(), 0L);
    }

    public void unload() {
        this.loaded = false;

<<<<<<< HEAD
        if (hologram != null) {
            this.hologram.removeHologram();
            this.hologram = null;
        }
=======
        this.hologram.removeHologramAndItem();
        this.hologram = null;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    public void cache() {
        DataManager.getInstance().getCache().getPlacedSpawners().put(location, this);
    }
}