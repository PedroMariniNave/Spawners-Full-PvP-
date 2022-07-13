package com.zpedroo.voltzspawners.objects;

import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
public class Spawner {

    private final EntityType entityType;
    private final String entityName;
    private final ItemStack item;
    private final String type;
    private final String typeTranslated;
    private final String displayName;
    private final BigInteger maximumStack;
    private final String permission;
    private final List<SpawnerDrop> drops;
    private final List<String> commands;
    private final int requiredLevel;
    private final int mcMMOExp;
    private final int spawnDelay;
    
    public ItemStack getItem(final int amount) {
        NBTItem nbt = new NBTItem(this.item.clone());
        nbt.setString("SpawnersType", this.getType());
        ItemStack item = nbt.getItem();
        if (item.getItemMeta() != null) {
            String displayName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : null;
            List<String> lore = item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : null;
            ItemMeta meta = item.getItemMeta();
            if (displayName != null) {
                meta.setDisplayName(StringUtils.replaceEach(displayName, new String[]{
                        "{required_level}",
                        "{mcmmo}"
                }, new String[]{
                        NumberFormatter.getInstance().formatDecimal(requiredLevel),
                        NumberFormatter.getInstance().format(BigInteger.valueOf(mcMMOExp))
                }));
            }

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
    
    public boolean isUnlocked(Player player) {
        return permission == null || player.hasPermission(permission);
    }
    
    public boolean hasSpecialDrops() {
        for (SpawnerDrop spawnerDrop : drops) {
            if (spawnerDrop.isSingleDrop()) return true;
        }

        return false;
    }
}