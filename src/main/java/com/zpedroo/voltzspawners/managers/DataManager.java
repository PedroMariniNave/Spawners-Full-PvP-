package com.zpedroo.voltzspawners.managers;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.cache.DataCache;
import com.zpedroo.voltzspawners.mysql.DBConnection;
import com.zpedroo.voltzspawners.objects.Drop;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.PlayerData;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.builder.ItemBuilder;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private final DataCache dataCache = new DataCache();

    public DataManager() {
        instance = this;
        this.loadConfigSpawners();
        VoltzSpawners.get().getServer().getScheduler().runTaskLaterAsynchronously(VoltzSpawners.get(), this::loadPlacedSpawners, 0L);
        VoltzSpawners.get().getServer().getScheduler().runTaskLaterAsynchronously(VoltzSpawners.get(), this::updateTopSpawners, 20L);
    }

    public PlayerData loadPlayerData(Player player) {
        PlayerData data = dataCache.getPlayerData().get(player);
        if (data == null) {
            data = DBConnection.getInstance().getDBManager().getPlayerData(player);
            dataCache.getPlayerData().put(player, data);
        }

        return data;
    }

    public void savePlayerData(Player player) {
        PlayerData data = dataCache.getPlayerData().remove(player);
        if (data == null || !data.isQueueUpdate()) return;

        DBConnection.getInstance().getDBManager().savePlayerData(data);
        data.setUpdate(false);
    }

    public void saveAllPlayersData() {
        new HashSet<>(dataCache.getPlayerData().keySet()).forEach(this::savePlayerData);
    }

    private void loadConfigSpawners() {
        File folder = new File(VoltzSpawners.get().getDataFolder(), "/spawners");
        File[] files = folder.listFiles((file, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File fl : files) {
            FileConfiguration file = YamlConfiguration.loadConfiguration(fl);

            EntityType entityType = EntityType.valueOf(file.getString("Spawner-Settings.entity-type"));
            String entityName = ChatColor.translateAlternateColorCodes('&', file.getString("Spawner-Settings.entity-name"));
            ItemStack item = ItemBuilder.build(file, "Spawner-Settings.item").build();
            String type = fl.getName().replace(".yml", "");
            String typeTranslated = file.getString("Spawner-Settings.type-translated");
            String displayName = ChatColor.translateAlternateColorCodes('&', file.getString("Spawner-Settings.entity-name"));
            BigInteger maxStack = NumberFormatter.getInstance().filter(file.getString("Spawner-Settings.max-stack"));
            String permission = file.getString("Spawner-Settings.permission", null);
            List<String> commands = file.getStringList("Spawner-Settings.commands");
            int requiredLevel = file.getInt("Spawner-Settings.required-level", 0);
            int mcMMOExp = file.getInt("Spawner-Settings.exp-mcmmo", 0);
            int spawnDelay = file.getInt("Spawner-Settings.spawn-delay");

            List<Drop> drops = new ArrayList<>(2);

            if (file.contains("Spawner-Settings.drops")) {
                for (String dropsName : file.getConfigurationSection("Spawner-Settings.drops").getKeys(false)) {
                    ItemStack displayItem = ItemBuilder.build(file, "Spawner-Settings.drops." + dropsName + ".display-item").build();
                    ItemStack itemToGive = null;
                    if (file.contains("Spawner-Settings.drops." + dropsName + ".item-to-give")) {
                        itemToGive = ItemBuilder.build(file, "Spawner-Settings.drops." + dropsName + ".item-to-give").build();
                    }

                    List<String> dropCommands = file.getStringList("Spawner-Settings.drops." + dropsName + ".commands");
                    boolean singleDrop = file.getBoolean("Spawner-Settings.drops." + dropsName + ".single-drop", false);
                    double chance = file.getDouble("Spawner-Settings.drops." + dropsName + ".chance", 0);

                    drops.add(new Drop(displayItem, itemToGive, dropCommands, singleDrop, chance));
                }
            }

            Spawner spawner = new Spawner(entityType, entityName, item, type, typeTranslated, displayName, maxStack, permission, drops, commands, requiredLevel, mcMMOExp, spawnDelay);
            cache(spawner);
        }
    }

    private void loadPlacedSpawners() {
        dataCache.setPlacedSpawners(DBConnection.getInstance().getDBManager().getPlacedSpawners());
    }

    public void updateTopSpawners() {
        dataCache.setTopSpawners(getTopSpawnersOrdered());
    }

    public void saveAllSpawnersData() {
        new HashSet<>(dataCache.getDeletedSpawners()).forEach(spawnerLocation -> {
            DBConnection.getInstance().getDBManager().deleteSpawner(spawnerLocation);
        });

        dataCache.getDeletedSpawners().clear();

        new HashSet<>(dataCache.getPlacedSpawners().values()).forEach(spawner -> {
            if (spawner == null) return;
            if (!spawner.isUpdate()) return;

            DBConnection.getInstance().getDBManager().saveSpawner(spawner);
            spawner.setUpdate(false);
        });
    }

    private void cache(Spawner spawner) {
        dataCache.getSpawners().put(spawner.getType().toUpperCase(), spawner);
    }

    private Map<UUID, BigInteger> getTopSpawnersOrdered() {
        Map<UUID, BigInteger> playerSpawners = new HashMap<>(dataCache.getPlacedSpawners().size());

        new HashSet<>(dataCache.getPlacedSpawners().values()).forEach(spawner -> {
            playerSpawners.put(spawner.getOwnerUUID(), spawner.getStack().add(playerSpawners.getOrDefault(spawner.getOwnerUUID(), BigInteger.ZERO)));
        });

        return orderMapByValue(playerSpawners, 10);
    }

    private Map<UUID, BigInteger> orderMapByValue(Map<UUID, BigInteger> map, int limit) {
        return map.entrySet().stream().sorted((value1, value2) -> value2.getValue().compareTo(value1.getValue())).limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value1, LinkedHashMap::new));
    }

    public DataCache getCache() {
        return dataCache;
    }

    public PlacedSpawner getPlacedSpawner(Location location) {
        if (dataCache.getPlacedSpawners() == null) return null;

        return dataCache.getPlacedSpawners().get(location);
    }

    public Spawner getSpawner(String type) {
        return dataCache.getSpawners().get(type.toUpperCase());
    }
}