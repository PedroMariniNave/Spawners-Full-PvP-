package com.zpedroo.voltzspawners;

<<<<<<< HEAD
import com.zpedroo.voltzspawners.commands.KillAllCmd;
import com.zpedroo.voltzspawners.commands.SpawnersCmd;
=======
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.zpedroo.voltzspawners.commands.SpawnersCmd;
import com.zpedroo.voltzspawners.hooks.ProtocolLibHook;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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
<<<<<<< HEAD
=======
import com.zpedroo.voltzspawners.utils.translator.DropTranslator;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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
<<<<<<< HEAD
        new DBConnection(getConfig());
        new SpawnerManager();
=======
        new DropTranslator(getConfig());
        new SpawnerManager();
        new DBConnection(getConfig());
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        new SpawnerTask(this);
        new SaveTask(this);
        new Menus();

<<<<<<< HEAD
=======
        ProtocolLibrary.getProtocolManager().addPacketListener(new ProtocolLibHook(this, PacketType.Play.Client.LOOK));

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        registerCommands();
        registerListeners();
    }

    public void onDisable() {
        if (!isMySQLEnabled(getConfig())) return;

        try {
<<<<<<< HEAD
            SpawnerManager.getInstance().clearAll();
            DataManager.getInstance().saveAllSpawnersData();
            DataManager.getInstance().saveAllPlayersData();
            DBConnection.getInstance().closeConnection();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "An error occurred while trying to save data!", ex);
=======
            DataManager.getInstance().saveAll();
            SpawnerManager.getInstance().clearAll();
            DBConnection.getInstance().closeConnection();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "An error occurred while trying to save data!");
            ex.printStackTrace();
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        }
    }

    private void registerCommands() {
        getCommand("spawners").setExecutor(new SpawnersCmd());
<<<<<<< HEAD
        getCommand("killall").setExecutor(new KillAllCmd());
=======
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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