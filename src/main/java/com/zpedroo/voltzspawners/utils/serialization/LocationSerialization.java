package com.zpedroo.voltzspawners.utils.serialization;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerialization implements ISerialization<Location> {

    @Override
    public String serialize(Location location) {
        if (location == null) return null;

        StringBuilder serialized = new StringBuilder(4);
        serialized.append(location.getWorld().getName());
        serialized.append("#" + location.getX());
        serialized.append("#" + location.getY());
        serialized.append("#" + location.getZ());

        return serialized.toString();
    }

    @Override
    public Location deserialize(String serialized) {
        if (serialized == null) return null;

        String[] locationSplit = serialized.split("#");
        World world = Bukkit.getWorld(locationSplit[0]);
        double x = Double.parseDouble(locationSplit[1]);
        double y = Double.parseDouble(locationSplit[2]);
        double z = Double.parseDouble(locationSplit[3]);

        return new Location(world, x, y, z);
    }
}
