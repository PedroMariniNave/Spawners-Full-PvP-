package com.zpedroo.voltzspawners.tasks;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.EntityManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigInteger;

import static com.zpedroo.voltzspawners.utils.config.Settings.SPAWNER_UPDATE;

public class SpawnerTask extends BukkitRunnable {

    public SpawnerTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, SPAWNER_UPDATE, SPAWNER_UPDATE);
    }

    @Override
    public void run() {
        DataManager.getInstance().getCache().getPlacedSpawners().values().stream().filter(placedSpawner ->
            placedSpawner != null && placedSpawner.isLoaded() &&
        placedSpawner.getLocation().getWorld().getChunkAt(placedSpawner.getLocation().getBlock()).isLoaded()).forEach(spawner -> {
            int delay = spawner.getSpawnDelay() - SPAWNER_UPDATE;
            spawner.setSpawnDelay(delay);

            if (delay >= 0) return;

            BigInteger amount = spawner.getStack();
            if (amount.signum() <= 0) amount = BigInteger.ONE;

            final BigInteger finalAmount = amount;
            VoltzSpawners.get().getServer().getScheduler().runTaskLater(VoltzSpawners.get(), () -> EntityManager.spawn(spawner, finalAmount), 0L);

            spawner.updateDelay();
        });
    }
}