package com.zpedroo.voltzspawners.objects;

<<<<<<< HEAD
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
=======
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.utils.config.Settings;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import com.zpedroo.voltzspawners.utils.translator.DropTranslator;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.math.BigInteger;

public class Drop {

    private Item item;
    private ItemStack itemStack;
    private PlacedSpawner spawner;
    private BigInteger stackAmount;

    public Drop(ItemStack itemStack, PlacedSpawner spawner, BigInteger stackAmount) {
        this.itemStack = itemStack;
        this.spawner = spawner;
        this.stackAmount = stackAmount;
        this.cache();
    }

    public Item getItem() {
        return item;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public PlacedSpawner getSpawner() {
        return spawner;
    }

    public BigInteger getStackAmount() {
        return stackAmount;
    }

    public void setStackAmount(BigInteger stackAmount) {
        this.stackAmount = stackAmount;
        if (stackAmount.signum() <= 0) {
            item.remove();
            return;
        }

        item.setCustomName(StringUtils.replaceEach(Settings.DROP_DISPLAY, new String[]{
                "{name}",
                "{translated_name}",
                "{amount}"
        }, new String[]{
                itemStack.getType().toString(),
                DropTranslator.getInstance().translate(itemStack),
                NumberFormatter.getInstance().format(stackAmount)
        }));
    }

    public void drop(Location location) {
        this.item = location.getWorld().dropItemNaturally(location, itemStack);

        item.setMetadata("***", new FixedMetadataValue(VoltzSpawners.get(), true));
        item.setMetadata("DropID", new FixedMetadataValue(VoltzSpawners.get(), hashCode()));
        item.setCustomName(StringUtils.replaceEach(Settings.DROP_DISPLAY, new String[]{
                "{name}",
                "{translated_name}",
                "{amount}"
        }, new String[]{
                itemStack.getType().toString(),
                DropTranslator.getInstance().translate(itemStack),
                NumberFormatter.getInstance().format(stackAmount)
        }));
        item.setCustomNameVisible(true);
    }

    private void cache() {
        DataManager.getInstance().getCache().getDrops().put(hashCode(), this);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }
}