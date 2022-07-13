package com.zpedroo.voltzspawners.tasks;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.utils.config.Settings;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTask extends BukkitRunnable {

    public SaveTask(VoltzSpawners voltzSpawners) {
        this.runTaskTimerAsynchronously(voltzSpawners, 20L * Settings.SAVE_INTERVAL, 20L * Settings.SAVE_INTERVAL);
    }
    
    public void run() {
        DataManager.getInstance().saveAllSpawnersData();
        DataManager.getInstance().updateTopSpawners();
    }
}
