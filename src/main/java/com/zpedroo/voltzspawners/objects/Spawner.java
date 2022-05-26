package com.zpedroo.voltzspawners.objects;

<<<<<<< HEAD
import com.zpedroo.onlinetime.api.OnlineTimeAPI;
=======
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.EntityType;
<<<<<<< HEAD
import org.bukkit.entity.Player;
=======
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Spawner {

<<<<<<< HEAD
    private final EntityType entityType;
    private final String entityName;
    private final ItemStack item;
    private final String type;
    private final String typeTranslated;
    private final String displayName;
    private final BigInteger maximumStack;
    private final String permission;
    private final List<Drop> drops;
    private final List<String> commands;
    private final int requiredLevel;
    private final int mcMMOExp;
    private final int spawnDelay;

    public Spawner(EntityType entityType, String entityName, ItemStack item, String type, String typeTranslated, String displayName, BigInteger maximumStack, String permission, List<Drop> drops, List<String> commands, int requiredLevel, int mcMMOExp, int spawnDelay) {
=======
    private EntityType entityType;
    private String entityName;
    private ItemStack item;
    private String type;
    private String typeTranslated;
    private String displayName;
    private BigInteger dropsAmount;
    private BigInteger dropsValue;
    private BigInteger maximumStack;
    private String permission;
    private List<ItemStack> drops;
    private List<String> commands;
    private int mcMMOExp;
    private int spawnDelay;

    public Spawner(EntityType entityType, String entityName, ItemStack item, String type, String typeTranslated, String displayName, BigInteger dropsAmount, BigInteger dropsValue, BigInteger maximumStack, String permission, List<ItemStack> drops, List<String> commands, int mcMMOExp, int spawnDelay) {
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        this.entityType = entityType;
        this.entityName = entityName;
        this.item = item;
        this.type = type;
        this.typeTranslated = typeTranslated;
        this.displayName = displayName;
<<<<<<< HEAD
=======
        this.dropsAmount = dropsAmount;
        this.dropsValue = dropsValue;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        this.maximumStack = maximumStack;
        this.permission = permission;
        this.drops = drops;
        this.commands = commands;
<<<<<<< HEAD
        this.requiredLevel = requiredLevel;
=======
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        this.mcMMOExp = mcMMOExp;
        this.spawnDelay = spawnDelay;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getEntityName() {
        return entityName;
    }

<<<<<<< HEAD
    public ItemStack getItem(int amount) {
        NBTItem nbt = new NBTItem(item.clone());
=======
    public ItemStack getDisplayItem() {
        return item.clone();
    }

    public ItemStack getItem(BigInteger amount) {
        NBTItem nbt = new NBTItem(item.clone());
        nbt.setString("SpawnersAmount", amount.toString());
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        nbt.setString("SpawnersType", getType());

        ItemStack item = nbt.getItem();
        if (item.getItemMeta() != null) {
            String displayName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : null;
            List<String> lore = item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : null;
            ItemMeta meta = item.getItemMeta();

            if (displayName != null) meta.setDisplayName(StringUtils.replaceEach(displayName, new String[]{
<<<<<<< HEAD
                    "{required_level}",
                    "{mcmmo}"
            }, new String[]{
                    NumberFormatter.getInstance().formatDecimal(requiredLevel),
                    NumberFormatter.getInstance().format(BigInteger.valueOf(mcMMOExp))
=======
                    "{amount}"
            }, new String[]{
                    NumberFormatter.getInstance().format(amount)
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
            }));

            if (lore != null) {
                List<String> newLore = new ArrayList<>(lore.size());

                for (String str : lore) {
                    newLore.add(StringUtils.replaceEach(str, new String[]{
<<<<<<< HEAD
                            "{required_level}",
                            "{mcmmo}"
                    }, new String[]{
                            NumberFormatter.getInstance().formatDecimal(requiredLevel),
                            NumberFormatter.getInstance().format(BigInteger.valueOf(mcMMOExp))
=======
                            "{amount}"
                    }, new String[]{
                            NumberFormatter.getInstance().format(amount)
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
                    }));
                }

                meta.setLore(newLore);
            }

            item.setItemMeta(meta);
        }

<<<<<<< HEAD
        item.setAmount(amount);

=======
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        return item;
    }

    public String getType() {
        return type;
    }

    public String getTypeTranslated() {
        return typeTranslated;
    }

    public String getDisplayName() {
        return displayName;
    }

<<<<<<< HEAD
=======
    public BigInteger getDropsAmount() {
        return dropsAmount;
    }

    public BigInteger getDropsValue() {
        return dropsValue;
    }

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    public BigInteger getMaximumStack() {
        return maximumStack;
    }

    public String getPermission() {
        return permission;
    }

<<<<<<< HEAD
    public List<Drop> getDrops() {
=======
    public List<ItemStack> getDrops() {
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        return drops;
    }

    public List<String> getCommands() {
        return commands;
    }

<<<<<<< HEAD
    public int getRequiredLevel() {
        return requiredLevel;
    }

=======
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    public int getMcMMOExp() {
        return mcMMOExp;
    }

    public int getSpawnDelay() {
        return spawnDelay;
    }
<<<<<<< HEAD

    public boolean isUnlocked(Player player) {
        if (permission != null && !player.hasPermission(permission)) return false;

        return OnlineTimeAPI.getLevel(player) >= requiredLevel;
    }

    public boolean hasSpecialDrops() {
        for (Drop drop : drops) {
            if (drop.isSingleDrop()) return true;
        }

        return false;
    }
=======
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
}