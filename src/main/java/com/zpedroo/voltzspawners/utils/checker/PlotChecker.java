package com.zpedroo.voltzspawners.utils.checker;

import com.intellectualcrafters.plot.object.Plot;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlotChecker {

    public static boolean canInteractInPlot(Player player, Location location) {
        com.intellectualcrafters.plot.object.Location plotLocation = new com.intellectualcrafters.plot.object.Location(location.getWorld().getName(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
        Plot plot = Plot.getPlot(plotLocation);

        return plot != null && plot.isAdded(player.getUniqueId());
    }
}