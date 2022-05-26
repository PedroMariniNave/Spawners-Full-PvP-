package com.zpedroo.voltzspawners.mysql;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    public static DBConnection getInstance() { return instance; }

<<<<<<< HEAD
    protected static final String SPAWNERS_TABLE = "spawners";
    protected static final String PLAYERS_TABLE = "spawners_players";

    private final HikariDataSource hikari = new HikariDataSource();
    private final DBManager dbManager = new DBManager();

    public DBConnection(FileConfiguration file) {
        instance = this;
=======
    protected static final String TABLE = "spawners";

    private HikariDataSource hikari;
    private DBManager dbManager;

    public DBConnection(FileConfiguration file) {
        instance = this;
        this.dbManager = new DBManager();
        this.hikari = new HikariDataSource();
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

        enable(file);
        getDBManager().createTable();
    }

    private void enable(FileConfiguration file) {
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", file.getString("MySQL.host"));
        hikari.addDataSourceProperty("port", file.getInt("MySQL.port"));
        hikari.addDataSourceProperty("databaseName", file.getString("MySQL.database"));
        hikari.addDataSourceProperty("user", file.getString("MySQL.username"));
        hikari.addDataSourceProperty("password", file.getString("MySQL.password"));
        hikari.setMaximumPoolSize(10);
    }

    public void closeConnection() {
<<<<<<< HEAD
=======
        if (hikari == null) return;

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        hikari.close();
    }

    public Connection getConnection() throws SQLException {
        return hikari.getConnection();
    }

    public DBManager getDBManager() {
        return dbManager;
    }
}