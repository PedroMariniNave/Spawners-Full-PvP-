package com.zpedroo.voltzspawners.objects;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.SerializatorManager;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.math.BigInteger;
import java.util.Optional;

import static com.zpedroo.voltzspawners.utils.config.Settings.STACK_RADIUS;

@Getter
public class DropItem {

    private final Plugin plugin = VoltzSpawners.get();
    private final SpawnerDrop drop;
    private final PlacedSpawner spawner;
    private BigInteger stackAmount;
    private final String serializedLocation;
    
    public DropItem(SpawnerDrop drop, PlacedSpawner spawner, BigInteger stackAmount) {
        this.drop = drop;
        this.spawner = spawner;
        this.stackAmount = stackAmount;
        this.serializedLocation = SerializatorManager.getLocationSerialization().serialize(spawner.getLocation());
    }

    private Item getItem() {
        Optional<Entity> entityFound = spawner.getLocation().getWorld().getNearbyEntities(spawner.getLocation(), STACK_RADIUS, STACK_RADIUS, STACK_RADIUS)
                .stream().filter(entity -> entity instanceof Item && entity.hasMetadata("DropSpawner") &&
                        entity.getMetadata("DropSpawner").get(0).asString().equals(serializedLocation) &&
                        ((Item) entity).getItemStack().isSimilar(drop.getDisplayItem())
        ).findAny();

        return (Item) entityFound.orElse(null);
    }

    public void setStackAmount(BigInteger stackAmount) {
        this.setStackAmount(stackAmount, getItem());
    }

    public void setStackAmount(BigInteger stackAmount, Item item) {
        this.stackAmount = stackAmount;

        if (item == null) return;
        if (stackAmount.signum() <= 0) {
            item.remove();
            return;
        }

        if (drop.getDisplayName() != null) {
            item.setCustomName(StringUtils.replaceEach(drop.getDisplayName(), new String[]{
                    "{amount}"
            }, new String[]{
                    NumberFormatter.getInstance().format(stackAmount)
            }));
        }
    }
    
    public void drop(Location location) {
        Item item = location.getWorld().dropItem(location, drop.getDisplayItem());
        item.setMetadata("DropItem", new FixedMetadataValue(plugin, this));
        item.setMetadata("DropSpawner", new FixedMetadataValue(plugin, serializedLocation));
        item.setMetadata("***", new FixedMetadataValue(plugin, true));
        if (drop.getDisplayName() != null) {
            item.setCustomName(StringUtils.replaceEach(drop.getDisplayName(), new String[]{
                    "{amount}"
            }, new String[]{
                    NumberFormatter.getInstance().format(stackAmount)
            }));
            item.setCustomNameVisible(true);
        }
    }
}