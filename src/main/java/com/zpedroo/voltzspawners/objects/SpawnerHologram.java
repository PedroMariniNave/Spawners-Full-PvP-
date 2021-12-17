package com.zpedroo.voltzspawners.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.utils.config.Settings;
import org.bukkit.entity.Item;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SpawnerHologram {

    private PlacedSpawner spawner;

    private String[] hologramLines;
    private TextLine[] textLines;
    private Item displayItem;

    private Hologram hologram;

    public SpawnerHologram(PlacedSpawner spawner) {
        this.spawner = spawner;
        this.hologramLines = Settings.SPAWNER_HOLOGRAM;
        this.updateHologramAndItem();
    }

    public void updateHologramAndItem() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateHologram();
                spawnItem();
            }
        }.runTaskLater(VoltzSpawners.get(), 0L);
    }

    public void removeHologramAndItem() {
        removeHologram();
        removeItem();
    }

    private void updateHologram() {
        if (hologram == null || hologram.isDeleted()) this.createHologram();

        for (int i = 0; i < hologramLines.length; i++) {
            textLines[i].setText(spawner.replace(hologramLines[i]));
        }
    }

    private void createHologram() {
        hologram = HologramsAPI.createHologram(VoltzSpawners.get(), spawner.getLocation().clone().add(0.5D, 3.15, 0.5D));
        textLines = new TextLine[hologramLines.length];

        for (int i = 0; i < hologramLines.length; i++) {
            textLines[i] = hologram.insertTextLine(i, spawner.replace(hologramLines[i]));
        }
    }

    private void removeHologram() {
        if (hologram == null || hologram.isDeleted()) return;

        hologram.delete();
        hologram = null;
    }

    private void spawnItem() {
        if (displayItem != null && !displayItem.isDead()) return;

        displayItem = spawner.getLocation().getWorld().dropItem(spawner.getLocation().clone().add(0.5D, 1D, 0.5D), spawner.getSpawner().getDisplayItem());
        displayItem.setVelocity(new Vector(0, 0.1, 0));
        displayItem.setPickupDelay(Integer.MAX_VALUE);
        displayItem.setMetadata("***", new FixedMetadataValue(VoltzSpawners.get(), true));
        displayItem.setCustomNameVisible(false);
    }

    private void removeItem() {
        if (displayItem == null) return;

        displayItem.remove();
        displayItem = null;
    }
}