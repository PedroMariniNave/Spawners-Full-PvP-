package com.zpedroo.voltzspawners.tasks;

<<<<<<< HEAD
=======
import com.zpedroo.voltzspawners.VoltzSpawners;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.EntityManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigInteger;

import static com.zpedroo.voltzspawners.utils.config.Settings.SPAWNER_UPDATE;

public class SpawnerTask extends BukkitRunnable {

    public SpawnerTask(Plugin plugin) {
<<<<<<< HEAD
        this.runTaskTimer(plugin, SPAWNER_UPDATE, SPAWNER_UPDATE);
=======
        this.runTaskTimerAsynchronously(plugin, SPAWNER_UPDATE, SPAWNER_UPDATE);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    @Override
    public void run() {
        DataManager.getInstance().getCache().getPlacedSpawners().values().stream().filter(placedSpawner ->
<<<<<<< HEAD
            placedSpawner != null && placedSpawner.isLoaded() && placedSpawner.getLocation().getWorld().isChunkLoaded(placedSpawner.getLocation().getChunk())
        ).forEach(spawner -> {
=======
            placedSpawner != null && placedSpawner.isLoaded() &&
        placedSpawner.getLocation().getWorld().getChunkAt(placedSpawner.getLocation().getBlock()).isLoaded()).forEach(spawner -> {
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
            int delay = spawner.getSpawnDelay() - SPAWNER_UPDATE;
            spawner.setSpawnDelay(delay);

            if (delay >= 0) return;

            BigInteger amount = spawner.getStack();
            if (amount.signum() <= 0) amount = BigInteger.ONE;

<<<<<<< HEAD
            EntityManager.spawn(spawner, amount);
=======
            final BigInteger finalAmount = amount;
            VoltzSpawners.get().getServer().getScheduler().runTaskLater(VoltzSpawners.get(), () -> EntityManager.spawn(spawner, finalAmount), 0L);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

            spawner.updateDelay();
        });
    }
}