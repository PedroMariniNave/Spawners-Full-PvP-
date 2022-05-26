package com.zpedroo.voltzspawners.objects;

import com.zpedroo.onlinetime.api.OnlineTimeAPI;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Spawner {

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
        this.entityType = entityType;
        this.entityName = entityName;
        this.item = item;
        this.type = type;
        this.typeTranslated = typeTranslated;
        this.displayName = displayName;
        this.maximumStack = maximumStack;
        this.permission = permission;
        this.drops = drops;
        this.commands = commands;
        this.requiredLevel = requiredLevel;
        this.mcMMOExp = mcMMOExp;
        this.spawnDelay = spawnDelay;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public ItemStack getItem(int amount) {
        NBTItem nbt = new NBTItem(item.clone());
        nbt.setString("SpawnersType", getType());

        ItemStack item = nbt.getItem();
        if (item.getItemMeta() != null) {
            String displayName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : null;
            List<String> lore = item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : null;
            ItemMeta meta = item.getItemMeta();

            if (displayName != null) meta.setDisplayName(StringUtils.replaceEach(displayName, new String[]{
                    "{required_level}",
                    "{mcmmo}"
            }, new String[]{
                    NumberFormatter.getInstance().formatDecimal(requiredLevel),
                    NumberFormatter.getInstance().format(BigInteger.valueOf(mcMMOExp))
            }));

            if (lore != null) {
                List<String> newLore = new ArrayList<>(lore.size());

                for (String str : lore) {
                    newLore.add(StringUtils.replaceEach(str, new String[]{
                            "{required_level}",
                            "{mcmmo}"
                    }, new String[]{
                            NumberFormatter.getInstance().formatDecimal(requiredLevel),
                            NumberFormatter.getInstance().format(BigInteger.valueOf(mcMMOExp))
                    }));
                }

                meta.setLore(newLore);
            }

            item.setItemMeta(meta);
        }

        item.setAmount(amount);

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

    public BigInteger getMaximumStack() {
        return maximumStack;
    }

    public String getPermission() {
        return permission;
    }

    public List<Drop> getDrops() {
        return drops;
    }

    public List<String> getCommands() {
        return commands;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public int getMcMMOExp() {
        return mcMMOExp;
    }

    public int getSpawnDelay() {
        return spawnDelay;
    }

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
}