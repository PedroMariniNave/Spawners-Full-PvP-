package com.zpedroo.voltzspawners.managers.cache;

import com.zpedroo.voltzspawners.objects.DropItem;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.PlayerData;
import com.zpedroo.voltzspawners.objects.Spawner;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.util.*;

public class DataCache {

    private final Map<Player, PlayerData> playerData = new HashMap<>(64);
    private final Map<String, Spawner> spawners = new HashMap<>(24);
    private Map<Location, PlacedSpawner> placedSpawners;
    private Map<UUID, BigInteger> topSpawners;
    private final Map<Long, DropItem> drops = new HashMap<>(16);;
    private final Set<Location> deletedSpawners = new HashSet<>(32);

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
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

    public Map<Long, DropItem> getDrops() {
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