package com.zpedroo.voltzspawners.mysql;

import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.SerializatorManager;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.PlayerData;
import com.zpedroo.voltzspawners.objects.Spawner;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DBManager {

    public void savePlayerData(PlayerData data) {
        executeUpdate("REPLACE INTO `" + DBConnection.PLAYERS_TABLE + "` (`uuid`, `kill_all`) VALUES " +
                "('" + data.getUniqueId() + "', " +
                "'" + (data.isKillAll() ? 1 : 0) + "');");
    }

    public PlayerData getPlayerData(Player player) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT * FROM `" + DBConnection.PLAYERS_TABLE + "` WHERE `uuid`='" + player.getUniqueId().toString() + "';";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                boolean killAll = result.getBoolean(2);

                return new PlayerData(player.getUniqueId(), killAll);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnections(connection, result, preparedStatement, null);
        }

        return new PlayerData(player.getUniqueId(), true);
    }

    public void saveSpawner(PlacedSpawner spawner) {
        executeUpdate("REPLACE INTO `" + DBConnection.SPAWNERS_TABLE + "` (`location`, `uuid`, `stack`, `type`) VALUES " +
                "('" + SerializatorManager.getLocationSerialization().serialize(spawner.getLocation()) + "', " +
                "'" + spawner.getOwnerUUID().toString() + "', " +
                "'" + spawner.getStack().toString() + "', " +
                "'" + spawner.getSpawner().getType() + "');");
    }

    public void deleteSpawner(Location location) {
        executeUpdate("DELETE FROM `" + DBConnection.SPAWNERS_TABLE + "` WHERE `location`='" + SerializatorManager.getLocationSerialization().serialize(location) + "';");
    }
    
    public Map<Location, PlacedSpawner> getPlacedSpawners() {
        Map<Location, PlacedSpawner> spawners = new HashMap<>(512);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT * FROM `spawners`;";
        try {
            connection = this.getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                Location location = SerializatorManager.getLocationSerialization().deserialize(result.getString(1));
                UUID ownerUUID = UUID.fromString(result.getString(2));
                BigInteger stack = result.getBigDecimal(3).toBigInteger();
//                Map<Upgrade, Integer> upgrades = SerializatorManager.getUpgradeSerialization().deserialize(result.getString(4));
                Spawner spawner = DataManager.getInstance().getSpawner(result.getString(4));

                spawners.put(location, new PlacedSpawner(location, ownerUUID, stack, /*upgrades,*/ spawner));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnections(connection, result, preparedStatement, null);
        }

        return spawners;
    }

    private void executeUpdate(String query) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnections(connection, null, null, statement);
        }
    }

    private void closeConnections(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement, Statement statement) {
        try {
            if (connection != null) connection.close();
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    protected void createTables() {
 //       executeUpdate("CREATE TABLE IF NOT EXISTS `spawners` (`location` VARCHAR(255), `uuid` VARCHAR(255), `stack` DECIMAL(40,0), `upgrades` TEXT, `type` VARCHAR(32), PRIMARY KEY(`location`));");
        executeUpdate("CREATE TABLE IF NOT EXISTS `spawners` (`location` VARCHAR(255), `uuid` VARCHAR(255), `stack` DECIMAL(40,0), `type` VARCHAR(32), PRIMARY KEY(`location`));");
        executeUpdate("CREATE TABLE IF NOT EXISTS `spawners_players` (`uuid` VARCHAR(255), `kill_all` BOOLEAN, PRIMARY KEY(`uuid`));");
    }
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }
}