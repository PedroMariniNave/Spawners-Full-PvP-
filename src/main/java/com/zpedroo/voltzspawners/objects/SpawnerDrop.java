package com.zpedroo.voltzspawners.objects;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public class SpawnerDrop {

    private final ItemStack displayItem;
    private final ItemStack itemToGive;
    private final List<String> commands;
    private final boolean singleDrop;
    private final double chance;

    public String getDisplayName() {
        return displayItem.getItemMeta().hasDisplayName() ? displayItem.getItemMeta().getDisplayName() : null;
    }
    
    public ItemStack getDisplayItem() {
        return displayItem == null ? null : displayItem.clone();
    }
    
    public ItemStack getItemToGive() {
        return itemToGive == null ? null : itemToGive.clone();
    }
}
