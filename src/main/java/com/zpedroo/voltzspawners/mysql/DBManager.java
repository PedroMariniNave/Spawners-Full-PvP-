package com.zpedroo.voltzspawners.mysql;

import com.zpedroo.voltzspawners.managers.DataManager;
<<<<<<< HEAD
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.PlayerData;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.serialization.LocationSerialization;
import org.bukkit.Location;
import org.bukkit.entity.Player;
=======
import com.zpedroo.voltzspawners.objects.Manager;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.serialization.LocationSerialization;
import org.bukkit.Location;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

import java.math.BigInteger;
import java.sql.*;
import java.util.HashMap;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import java.util.Map;
import java.util.UUID;

public class DBManager extends DataManager {

<<<<<<< HEAD
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
                "('" + LocationSerialization.serializeLocation(spawner.getLocation()) + "', " +
                "'" + spawner.getOwnerUUID().toString() + "', " +
                "'" + spawner.getStack().toString() + "', " +
                "'" + spawner.getSpawner().getType() + "');");
    }

    public void deleteSpawner(Location location) {
        executeUpdate("DELETE FROM `" + DBConnection.SPAWNERS_TABLE + "` WHERE `location`='" + LocationSerialization.serializeLocation(location) + "';");
=======
    public void saveSpawner(PlacedSpawner spawner) {
        if (contains(LocationSerialization.serializeLocation(spawner.getLocation()), "location")) {
            String query = "UPDATE `" + DBConnection.TABLE + "` SET" +
                    "`location`='" + LocationSerialization.serializeLocation(spawner.getLocation()) + "', " +
                    "`uuid`='" + spawner.getOwnerUUID().toString() + "', " +
                    "`stack`='" + spawner.getStack().toString() + "', " +
                    "`type`='" + spawner.getSpawner().getType() + "', " +
                    "`managers`='" + serializeManagers(spawner.getManagers()) + "', " +
                    "`public`='" + (spawner.isPublic() ? 1 : 0) + "' " +
                    "WHERE `location`='" + LocationSerialization.serializeLocation(spawner.getLocation()) + "';";
            executeUpdate(query);
            return;
        }

        String query = "INSERT INTO `" + DBConnection.TABLE + "` (`location`, `uuid`, `stack`, `type`, `managers`, `public`) VALUES " +
                "('" + LocationSerialization.serializeLocation(spawner.getLocation()) + "', " +
                "'" + spawner.getOwnerUUID().toString() + "', " +
                "'" + spawner.getStack().toString() + "', " +
                "'" + spawner.getSpawner().getType() + "', " +
                "'" + serializeManagers(spawner.getManagers()) + "', " +
                "'" + (spawner.isPublic() ? 1 : 0) + "');";
        executeUpdate(query);
    }

    public void deleteSpawner(Location location) {
        String query = "DELETE FROM `" + DBConnection.TABLE + "` WHERE `location`='" + LocationSerialization.serializeLocation(location) + "';";
        executeUpdate(query);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    public Map<Location, PlacedSpawner> getPlacedSpawners() {
        Map<Location, PlacedSpawner> spawners = new HashMap<>(512);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
<<<<<<< HEAD
        String query = "SELECT * FROM `" + DBConnection.SPAWNERS_TABLE + "`;";
=======
        String query = "SELECT * FROM `" + DBConnection.TABLE + "`;";
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while (result.next()) {
                Location location = LocationSerialization.deserializeLocation(result.getString(1));
                UUID ownerUUID = UUID.fromString(result.getString(2));
                BigInteger stack = result.getBigDecimal(3).toBigInteger();
                Spawner spawner = getSpawner(result.getString(4));
<<<<<<< HEAD

                spawners.put(location, new PlacedSpawner(location, ownerUUID, stack, spawner));
=======
                List<Manager> managers = deserializeManagers(result.getString(5));
                boolean publicSpawner = result.getBoolean(6);

                spawners.put(location, new PlacedSpawner(location, ownerUUID, stack, spawner, managers, publicSpawner));
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnections(connection, result, preparedStatement, null);
        }

        return spawners;
    }

<<<<<<< HEAD
=======
    private Boolean contains(String value, String column) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT `" + column + "` FROM `" + DBConnection.TABLE + "` WHERE `" + column + "`='" + value + "';";
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnections(connection, result, preparedStatement, null);
        }

        return false;
    }

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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

    protected void createTable() {
<<<<<<< HEAD
        executeUpdate("CREATE TABLE IF NOT EXISTS `" + DBConnection.SPAWNERS_TABLE + "` (`location` VARCHAR(255), `uuid` VARCHAR(255), `stack` DECIMAL(40,0), `type` VARCHAR(32), PRIMARY KEY(`location`));");
        executeUpdate("CREATE TABLE IF NOT EXISTS `" + DBConnection.PLAYERS_TABLE + "` (`uuid` VARCHAR(255), `kill_all` BOOLEAN, PRIMARY KEY(`uuid`));");
=======
        String query = "CREATE TABLE IF NOT EXISTS `" + DBConnection.TABLE + "` (`location` VARCHAR(255), `uuid` VARCHAR(255), `stack` DECIMAL(40,0), `type` VARCHAR(32), `managers` LONGTEXT, `public` BOOLEAN, PRIMARY KEY(`location`));";
        executeUpdate(query);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }
}