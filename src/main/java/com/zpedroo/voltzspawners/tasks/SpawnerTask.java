package com.zpedroo.voltzspawners.tasks;

import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.EntityManager;
import com.zpedroo.voltzspawners.utils.config.Settings;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigInteger;

public class SpawnerTask extends BukkitRunnable {

    public SpawnerTask(Plugin plugin) {
        this.runTaskTimer(plugin, Settings.SPAWNER_UPDATE, Settings.SPAWNER_UPDATE);
    }
    
    public void run() {
        DataManager.getInstance().getCache().getPlacedSpawners().values().stream().filter(placedSpawner ->
                placedSpawner != null && placedSpawner.isLoaded() &&
                        placedSpawner.getLocation().getWorld().isChunkLoaded(placedSpawner.getLocation().getBlockX() >> 4, placedSpawner.getLocation().getBlockZ() >> 4)
        ).forEach(spawner -> {
            int delay = spawner.getSpawnDelay() - Settings.SPAWNER_UPDATE;
            spawner.setSpawnDelay(delay);
            if (delay > 0) return;

            BigInteger amount = spawner.getStack();
            if (amount.signum() <= 0) amount = BigInteger.ONE;

            EntityManager.spawn(spawner, amount);
            spawner.updateDelay();
        });
    }
}