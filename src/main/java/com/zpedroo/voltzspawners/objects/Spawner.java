package com.zpedroo.voltzspawners.objects;

import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Spawner {

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
        this.entityType = entityType;
        this.entityName = entityName;
        this.item = item;
        this.type = type;
        this.typeTranslated = typeTranslated;
        this.displayName = displayName;
        this.dropsAmount = dropsAmount;
        this.dropsValue = dropsValue;
        this.maximumStack = maximumStack;
        this.permission = permission;
        this.drops = drops;
        this.commands = commands;
        this.mcMMOExp = mcMMOExp;
        this.spawnDelay = spawnDelay;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public ItemStack getDisplayItem() {
        return item.clone();
    }

    public ItemStack getItem(BigInteger amount) {
        NBTItem nbt = new NBTItem(item.clone());
        nbt.setString("SpawnersAmount", amount.toString());
        nbt.setString("SpawnersType", getType());

        ItemStack item = nbt.getItem();
        if (item.getItemMeta() != null) {
            String displayName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : null;
            List<String> lore = item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : null;
            ItemMeta meta = item.getItemMeta();

            if (displayName != null) meta.setDisplayName(StringUtils.replaceEach(displayName, new String[]{
                    "{amount}"
            }, new String[]{
                    NumberFormatter.getInstance().format(amount)
            }));

            if (lore != null) {
                List<String> newLore = new ArrayList<>(lore.size());

                for (String str : lore) {
                    newLore.add(StringUtils.replaceEach(str, new String[]{
                            "{amount}"
                    }, new String[]{
                            NumberFormatter.getInstance().format(amount)
                    }));
                }

                meta.setLore(newLore);
            }

            item.setItemMeta(meta);
        }

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

    public BigInteger getDropsAmount() {
        return dropsAmount;
    }

    public BigInteger getDropsValue() {
        return dropsValue;
    }

    public BigInteger getMaximumStack() {
        return maximumStack;
    }

    public String getPermission() {
        return permission;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public List<String> getCommands() {
        return commands;
    }

    public int getMcMMOExp() {
        return mcMMOExp;
    }

    public int getSpawnDelay() {
        return spawnDelay;
    }
}