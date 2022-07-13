package com.zpedroo.voltzspawners.managers.cache;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.math.*;
import com.zpedroo.voltzspawners.objects.*;
import java.util.*;

@Getter
@Setter
public class DataCache {

    private final Set<Location> deletedSpawners = new HashSet<>(32);
    private final Map<Player, PlayerData> playerData = new HashMap<>(64);
    private final Map<String, Spawner> spawners = new HashMap<>(24);
    private Map<Location, PlacedSpawner> placedSpawners;
    private Map<UUID, BigInteger> topSpawners;
}