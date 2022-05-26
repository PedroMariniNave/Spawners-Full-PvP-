package com.zpedroo.voltzspawners;

import com.zpedroo.voltzspawners.commands.KillAllCmd;
import com.zpedroo.voltzspawners.commands.SpawnersCmd;
import com.zpedroo.voltzspawners.listeners.EntityListeners;
import com.zpedroo.voltzspawners.listeners.PlayerChatListener;
import com.zpedroo.voltzspawners.listeners.PlayerGeneralListeners;
import com.zpedroo.voltzspawners.listeners.SpawnerListeners;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.SpawnerManager;
import com.zpedroo.voltzspawners.mysql.DBConnection;
import com.zpedroo.voltzspawners.tasks.SaveTask;
import com.zpedroo.voltzspawners.tasks.SpawnerTask;
import com.zpedroo.voltzspawners.utils.FileUtils;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import com.zpedroo.voltzspawners.utils.menu.Menus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class VoltzSpawners extends JavaPlugin {

    private static VoltzSpawners instance;
    public static VoltzSpawners get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);

        if (!isMySQLEnabled(getConfig())) {
            getLogger().log(Level.SEVERE, "MySQL are disabled! You need to enable it.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new NumberFormatter(getConfig());
        new DBConnection(getConfig());
        new SpawnerManager();
        new SpawnerTask(this);
        new SaveTask(this);
        new Menus();

        registerCommands();
        registerListeners();
    }

    public void onDisable() {
        if (!isMySQLEnabled(getConfig())) return;

        try {
            SpawnerManager.getInstance().clearAll();
            DataManager.getInstance().saveAllSpawnersData();
            DataManager.getInstance().saveAllPlayersData();
            DBConnection.getInstance().closeConnection();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "An error occurred while trying to save data!", ex);
        }
    }

    private void registerCommands() {
        getCommand("spawners").setExecutor(new SpawnersCmd());
        getCommand("killall").setExecutor(new KillAllCmd());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new EntityListeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
        getServer().getPluginManager().registerEvents(new SpawnerListeners(), this);
    }

    private boolean isMySQLEnabled(FileConfiguration file) {
        if (!file.contains("MySQL.enabled")) return false;

        return file.getBoolean("MySQL.enabled");
    }
}