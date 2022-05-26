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
    private final TextLine[] textLines = new TextLine[hologramLines.length];

    private Hologram hologram;

    public SpawnerHologram(PlacedSpawner spawner) {
        this.spawner = spawner;
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
        if (hologram == null || hologram.isDeleted()) return;

        hologram.delete();
        hologram = null;
    }

    private void sync(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(VoltzSpawners.get(), 0L);
    }
}