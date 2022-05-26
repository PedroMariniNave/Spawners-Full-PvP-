package com.zpedroo.voltzspawners.managers;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.cache.DataCache;
import com.zpedroo.voltzspawners.mysql.DBConnection;
<<<<<<< HEAD
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
=======
import com.zpedroo.voltzspawners.objects.Manager;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.builder.ItemBuilder;
import com.zpedroo.voltzspawners.utils.enums.Permission;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

<<<<<<< HEAD
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
=======
    private DataCache dataCache;

    public DataManager() {
        instance = this;
        this.dataCache = new DataCache();
        this.loadConfigSpawners();
        VoltzSpawners.get().getServer().getScheduler().runTaskLaterAsynchronously(VoltzSpawners.get(), this::loadPlacedSpawners, 20L);
        VoltzSpawners.get().getServer().getScheduler().runTaskLaterAsynchronously(VoltzSpawners.get(), this::updateTopSpawners, 40L);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    private void loadConfigSpawners() {
        File folder = new File(VoltzSpawners.get().getDataFolder(), "/spawners");
        File[] files = folder.listFiles((file, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File fl : files) {
<<<<<<< HEAD
=======
            if (fl == null) continue;

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
            FileConfiguration file = YamlConfiguration.loadConfiguration(fl);

            EntityType entityType = EntityType.valueOf(file.getString("Spawner-Settings.entity-type"));
            String entityName = ChatColor.translateAlternateColorCodes('&', file.getString("Spawner-Settings.entity-name"));
            ItemStack item = ItemBuilder.build(file, "Spawner-Settings.item").build();
            String type = fl.getName().replace(".yml", "");
            String typeTranslated = file.getString("Spawner-Settings.type-translated");
            String displayName = ChatColor.translateAlternateColorCodes('&', file.getString("Spawner-Settings.entity-name"));
<<<<<<< HEAD
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
=======
            BigInteger amount = NumberFormatter.getInstance().filter(file.getString("Spawner-Settings.drops-amount"));
            BigInteger dropsValue = NumberFormatter.getInstance().filter(file.getString("Spawner-Settings.drops-price"));
            BigInteger maxStack = NumberFormatter.getInstance().filter(file.getString("Spawner-Settings.max-stack"));
            String permission = file.getString("Spawner-Settings.permission", null);
            List<ItemStack> drops = new ArrayList<>(2);
            for (String drop : file.getConfigurationSection("Spawner-Settings.drops").getKeys(false)) {
                ItemStack dropItem = ItemBuilder.build(file, "Spawner-Settings.drops." + drop + ".item").build();

                drops.add(dropItem);
            }

            List<String> commands = file.getStringList("Spawner-Settings.commands");
            int mcMMOExp = file.getInt("Spawner-Settings.exp-mcmmo", 0);
            int spawnDelay = file.getInt("Spawner-Settings.spawn-delay");

            Spawner spawner = new Spawner(entityType, entityName, item, type, typeTranslated, displayName, amount, dropsValue, maxStack, permission, drops, commands, mcMMOExp, spawnDelay);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
            cache(spawner);
        }
    }

    private void loadPlacedSpawners() {
        dataCache.setPlacedSpawners(DBConnection.getInstance().getDBManager().getPlacedSpawners());
    }

    public void updateTopSpawners() {
        dataCache.setTopSpawners(getTopSpawnersOrdered());
    }

<<<<<<< HEAD
    public void saveAllSpawnersData() {
=======
    public void saveAll() {
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        new HashSet<>(dataCache.getDeletedSpawners()).forEach(spawnerLocation -> {
            DBConnection.getInstance().getDBManager().deleteSpawner(spawnerLocation);
        });

        dataCache.getDeletedSpawners().clear();

        new HashSet<>(dataCache.getPlacedSpawners().values()).forEach(spawner -> {
            if (spawner == null) return;
<<<<<<< HEAD
            if (!spawner.isUpdate()) return;
=======
            if (!spawner.isQueueUpdate()) return;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

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

<<<<<<< HEAD
    private Map<UUID, BigInteger> orderMapByValue(Map<UUID, BigInteger> map, int limit) {
=======
    private Map<UUID, BigInteger> orderMapByValue(Map<UUID, BigInteger> map, Integer limit) {
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        return map.entrySet().stream().sorted((value1, value2) -> value2.getValue().compareTo(value1.getValue())).limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value1, LinkedHashMap::new));
    }

<<<<<<< HEAD
=======
    public String serializeManagers(List<Manager> managers) {
        if (managers == null || managers.isEmpty()) return "";

        StringBuilder serialized = new StringBuilder(32);

        for (Manager manager : managers) {
            serialized.append(manager.getUUID().toString()).append("#");

            for (Permission permission : manager.getPermissions()) {
                serialized.append(permission.toString()).append("#");
            }

            serialized.append(",");
        }

        return serialized.toString();
    }

    public List<Manager> deserializeManagers(String managers) {
        if (managers == null || managers.isEmpty()) return new ArrayList<>(5);

        List<Manager> ret = new ArrayList<>(64);
        String[] split = managers.split(",");

        for (String str : split) {
            if (str == null) break;

            String[] managersSplit = str.split("#");

            List<Permission> permissions = new ArrayList<>(5);
            if (managersSplit.length > 1) {
                for (int i = 1; i < managersSplit.length; ++i) {
                    permissions.add(Permission.valueOf(managersSplit[i]));
                }
            }

            ret.add(new Manager(UUID.fromString(managersSplit[0]), permissions));
        }

        return ret;
    }

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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