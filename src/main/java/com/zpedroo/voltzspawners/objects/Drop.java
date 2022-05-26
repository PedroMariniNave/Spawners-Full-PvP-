package com.zpedroo.voltzspawners.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Drop {

    private final ItemStack displayItem;
    private final ItemStack itemToGive;
    private final List<String> commands;
    private final boolean singleDrop;
    private final double chance;

    public Drop(ItemStack displayItem, ItemStack itemToGive, List<String> commands, boolean singleDrop, double chance) {
        this.displayItem = displayItem;
        this.itemToGive = itemToGive;
        this.commands = commands;
        this.singleDrop = singleDrop;
        this.chance = chance;
    }

    public String getDisplayName() {
        return displayItem.getItemMeta().hasDisplayName() ? displayItem.getItemMeta().getDisplayName() : null;
    }

    public ItemStack getDisplayItem() {
        return displayItem == null ? null : displayItem.clone();
    }

    public ItemStack getItemToGive() {
        return itemToGive == null ? null : itemToGive.clone();
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean isSingleDrop() {
        return singleDrop;
    }

    public double getChance() {
        return chance;
    }
}