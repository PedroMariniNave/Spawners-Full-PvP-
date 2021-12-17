package com.zpedroo.voltzspawners.managers;

import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.math.BigInteger;

import static com.zpedroo.voltzspawners.utils.config.Settings.STACK_RADIUS;

public class SpawnerManager {

    private static SpawnerManager instance;
    public static SpawnerManager getInstance() { return instance; }

    public SpawnerManager() {
        instance = this;
    }

    public void clearAll() {
        DataManager.getInstance().getCache().getPlacedSpawners().values().stream().filter(placedSpawner ->
                placedSpawner.isLoaded() && placedSpawner.getHologram() != null).forEach(spawner -> {
                    spawner.getHologram().removeHologramAndItem();
                    spawner.removeEntities();
                });
    }

    public Object[] getNearSpawners(Player player, Block block, BigInteger addAmount, String type) {
        int radius = STACK_RADIUS;
        if (radius <= 0) return null;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block blocks = block.getRelative(x, y, z);
                    if (blocks.getType().equals(Material.AIR)) continue;

                    PlacedSpawner spawner = DataManager.getInstance().getPlacedSpawner(blocks.getLocation());
                    if (spawner == null) continue;
                    if (!StringUtils.equals(type, spawner.getSpawner().getType())) continue;
                    if (spawner.hasReachStackLimit()) continue;
                    if (!spawner.canInteract(player)) continue;

                    BigInteger overLimit = BigInteger.ZERO;
                    if (spawner.getSpawner().getMaximumStack().signum() > 0 && spawner.getStack().add(addAmount).compareTo(spawner.getSpawner().getMaximumStack()) > 0) {
                        overLimit = spawner.getStack().add(addAmount).subtract(spawner.getSpawner().getMaximumStack());
                    }

                    return new Object[] { spawner, overLimit };
                }
            }
        }

        return null;
    }
}
