package com.zpedroo.voltzspawners.managers.cache;

import com.zpedroo.voltzspawners.objects.Drop;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.Spawner;
import org.bukkit.Location;

import java.math.BigInteger;
import java.util.*;

public class DataCache {

    private Map<String, Spawner> spawners;
    private Map<Location, PlacedSpawner> placedSpawners;
    private Map<UUID, BigInteger> topSpawners;
    private Map<Integer, Drop> drops;
    private Set<Location> deletedSpawners;

    public DataCache() {
        this.spawners = new HashMap<>(24);
        this.drops = new HashMap<>(64);
        this.deletedSpawners = new HashSet<>(32);
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

    public Map<Integer, Drop> getDrops() {
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