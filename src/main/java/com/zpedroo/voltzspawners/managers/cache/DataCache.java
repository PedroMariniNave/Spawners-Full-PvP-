package com.zpedroo.voltzspawners.managers.cache;

<<<<<<< HEAD
import com.zpedroo.voltzspawners.objects.DropItem;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.PlayerData;
import com.zpedroo.voltzspawners.objects.Spawner;
import org.bukkit.Location;
import org.bukkit.entity.Player;
=======
import com.zpedroo.voltzspawners.objects.Drop;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.Spawner;
import org.bukkit.Location;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

import java.math.BigInteger;
import java.util.*;

public class DataCache {

<<<<<<< HEAD
    private final Map<Player, PlayerData> playerData = new HashMap<>(64);
    private final Map<String, Spawner> spawners = new HashMap<>(24);
    private Map<Location, PlacedSpawner> placedSpawners;
    private Map<UUID, BigInteger> topSpawners;
    private final Map<Long, DropItem> drops = new HashMap<>(16);;
    private final Set<Location> deletedSpawners = new HashSet<>(32);

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
=======
    private Map<String, Spawner> spawners;
    private Map<Location, PlacedSpawner> placedSpawners;
    private Map<UUID, BigInteger> topSpawners;
    private Map<Integer, Drop> drops;
    private Set<Location> deletedSpawners;

    public DataCache() {
        this.spawners = new HashMap<>(24);
        this.drops = new HashMap<>(64);
        this.deletedSpawners = new HashSet<>(32);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    public Map<String, Spawner> getSpawners() {
        return spawners;
    }

    public Map<Location, PlacedSpawner> getPlacedSpawners() {
        return placedSpawners;
    }

    public Map<UUID, BigInteger> getTopSpawners() {
        return topSpawners;
    }

<<<<<<< HEAD
    public Map<Long, DropItem> getDrops() {
=======
    public Map<Integer, Drop> getDrops() {
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        return drops;
    }

    public Set<Location> getDeletedSpawners() {
        return deletedSpawners;
    }

    public void setPlacedSpawners(Map<Location, PlacedSpawner> playerSpawners) {
        this.placedSpawners = playerSpawners;
    }

    public void setTopSpawners(Map<UUID, BigInteger> topSpawners) {
        this.topSpawners = topSpawners;
    }
}