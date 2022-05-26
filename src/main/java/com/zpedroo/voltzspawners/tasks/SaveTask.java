package com.zpedroo.voltzspawners.tasks;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.DataManager;
import org.bukkit.scheduler.BukkitRunnable;

import static com.zpedroo.voltzspawners.utils.config.Settings.*;

public class SaveTask extends BukkitRunnable {

    public SaveTask(VoltzSpawners voltzSpawners) {
        this.runTaskTimerAsynchronously(voltzSpawners, 20 * SAVE_INTERVAL, 20 * SAVE_INTERVAL);
    }

    @Override
    public void run() {
<<<<<<< HEAD
        DataManager.getInstance().saveAllSpawnersData();
=======
        DataManager.getInstance().saveAll();
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        DataManager.getInstance().updateTopSpawners();
    }
}