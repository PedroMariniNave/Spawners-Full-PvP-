package com.zpedroo.voltzspawners.managers;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.cache.DataCache;
import com.zpedroo.voltzspawners.mysql.DBConnection;
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
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private DataCache dataCache;

    public DataManager() {
        instance = this;
        this.dataCache = new DataCache();
        this.loadConfigSpawners();
        VoltzSpawners.get().getServer().getScheduler().runTaskLaterAsynchronously(VoltzSpawners.get(), this::loadPlacedSpawners, 20L);
        VoltzSpawners.get().getServer().getScheduler().runTaskLaterAsynchronously(VoltzSpawners.get(), this::updateTopSpawners, 40L);
    }

    private void loadConfigSpawners() {
        File folder = new File(VoltzSpawners.get().getDataFolder(), "/spawners");
        File[] files = folder.listFiles((file, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File fl : files) {
            if (fl == null) continue;

            FileConfiguration file = YamlConfiguration.loadConfiguration(fl);

            EntityType entityType = EntityType.valueOf(file.getString("Spawner-Settings.entity-type"));
            String entityName = ChatColor.translateAlternateColorCodes('&', file.getString("Spawner-Settings.entity-name"));
            ItemStack item = ItemBuilder.build(file, "Spawner-Settings.item").build();
            String type = fl.getName().replace(".yml", "");
            String typeTranslated = file.getString("Spawner-Settings.type-translated");
            String displayName = ChatColor.translateAlternateColorCodes('&', file.getString("Spawner-Settings.entity-name"));
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
            cache(spawner);
        }
    }

    private void loadPlacedSpawners() {
        dataCache.setPlacedSpawners(DBConnection.getInstance().getDBManager().getPlacedSpawners());
    }

    public void updateTopSpawners() {
        dataCache.setTopSpawners(getTopSpawnersOrdered());
    }

    public void saveAll() {
        new HashSet<>(dataCache.getDeletedSpawners()).forEach(spawnerLocation -> {
            DBConnection.getInstance().getDBManager().deleteSpawner(spawnerLocation);
        });

        dataCache.getDeletedSpawners().clear();

        new HashSet<>(dataCache.getPlacedSpawners().values()).forEach(spawner -> {
            if (spawner == null) return;
            if (!spawner.isQueueUpdate()) return;

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

    private Map<UUID, BigInteger> orderMapByValue(Map<UUID, BigInteger> map, Integer limit) {
        return map.entrySet().stream().sorted((value1, value2) -> value2.getValue().compareTo(value1.getValue())).limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value1, LinkedHashMap::new));
    }

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