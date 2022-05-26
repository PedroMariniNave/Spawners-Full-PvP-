package com.zpedroo.voltzspawners.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.utils.config.Settings;
<<<<<<< HEAD
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnerHologram {

    private final PlacedSpawner spawner;

    private final String[] hologramLines = Settings.SPAWNER_HOLOGRAM;
    private final TextLine[] textLines = new TextLine[hologramLines.length];
=======
import org.bukkit.entity.Item;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SpawnerHologram {

    private PlacedSpawner spawner;

    private String[] hologramLines;
    private TextLine[] textLines;
    private Item displayItem;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

    private Hologram hologram;

    public SpawnerHologram(PlacedSpawner spawner) {
        this.spawner = spawner;
<<<<<<< HEAD
        this.updateHologram();
    }

    private void createHologram() {
        sync(() -> {
            hologram = HologramsAPI.createHologram(VoltzSpawners.get(), spawner.getLocation().clone().add(0.5D, 2.75D, 0.5D));

            for (int i = 0; i < hologramLines.length; i++) {
                textLines[i] = hologram.insertTextLine(i, spawner.replace(hologramLines[i]));
            }
        });
    }

    public void updateHologram() {
        if (hologram == null || hologram.isDeleted()) {
            this.createHologram();
            return;
        }

        sync(() -> {
            for (int i = 0; i < hologramLines.length; i++) {
                textLines[i].setText(spawner.replace(hologramLines[i]));
            }
        });
    }

    public void removeHologram() {
=======
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
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        if (hologram == null || hologram.isDeleted()) return;

        hologram.delete();
        hologram = null;
    }

<<<<<<< HEAD
    private void sync(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(VoltzSpawners.get(), 0L);
=======
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
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }
}