package com.zpedroo.voltzspawners.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.utils.config.Settings;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnerHologram {

    private final PlacedSpawner spawner;
    private final String[] hologramLines = Settings.SPAWNER_HOLOGRAM;
    private final TextLine[] textLines = new TextLine[this.hologramLines.length];
    private Hologram hologram;
    
    public SpawnerHologram(PlacedSpawner spawner) {
        this.spawner = spawner;
        this.updateHologram();
    }
    
    private void createHologram() {
        sync(() -> {
            this.hologram = HologramsAPI.createHologram(VoltzSpawners.get(), spawner.getLocation().clone().add(0.5D, 2.75D, 0.5D));
            for (int i = 0; i < hologramLines.length; ++i) {
                this.textLines[i] = hologram.insertTextLine(i, spawner.replacePlaceholders(hologramLines[i]));
            }
        });
    }
    
    public void updateHologram() {
        if (hologram == null || hologram.isDeleted()) {
            createHologram();
            return;
        }

        sync(() -> {
            for (int i = 0; i < hologramLines.length; ++i) {
                this.textLines[i].setText(spawner.replacePlaceholders(hologramLines[i]));
            }
        });
    }
    
    public void removeHologram() {
        if (hologram == null || hologram.isDeleted()) return;

        hologram.delete();
        hologram = null;
    }
    
    private void sync(Runnable runnable) {
        new BukkitRunnable() {
            public void run() {
                runnable.run();
            }
        }.runTaskLater(VoltzSpawners.get(), 0L);
    }
}