package com.zpedroo.voltzspawners.objects;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.metadata.FixedMetadataValue;

import java.math.BigInteger;

public class DropItem {

    private Item item;
    private final Drop drop;
    private final PlacedSpawner spawner;
    private BigInteger stackAmount;
    private final long dropId;

    public DropItem(Drop drop, PlacedSpawner spawner, BigInteger stackAmount) {
        this.drop = drop;
        this.spawner = spawner;
        this.stackAmount = stackAmount;
        this.dropId = System.nanoTime();
        this.cache();
    }

    public Item getItem() {
        return item;
    }

    public Drop getDrop() {
        return drop;
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

        if (drop.getDisplayName() != null) item.setCustomName(StringUtils.replaceEach(drop.getDisplayName(), new String[]{
                "{amount}"
        }, new String[]{
                NumberFormatter.getInstance().format(stackAmount)
        }));
    }

    public void dropAndCache(Location location) {
        this.cache();
        this.item = location.getWorld().dropItemNaturally(location, drop.getDisplayItem());
        this.item.setMetadata("DropID", new FixedMetadataValue(VoltzSpawners.get(), dropId));
        this.item.setMetadata("***", new FixedMetadataValue(VoltzSpawners.get(), true));

        if (drop.getDisplayName() != null) {
            item.setCustomName(StringUtils.replaceEach(drop.getDisplayName(), new String[]{
                    "{amount}"
            }, new String[]{
                    NumberFormatter.getInstance().format(stackAmount)
            }));
            item.setCustomNameVisible(true);
        }
    }

    private void cache() {
        DataManager.getInstance().getCache().getDrops().put(dropId, this);
    }
}