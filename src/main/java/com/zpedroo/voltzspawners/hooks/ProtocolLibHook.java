package com.zpedroo.voltzspawners.hooks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;

public class ProtocolLibHook extends PacketAdapter {

    public ProtocolLibHook(Plugin plugin, PacketType packetType) {
        super(plugin, packetType);
    }

    public void onPacketReceiving(PacketEvent event) {
        /*
        Player player = event.getPlayer();
        Block block = player.getTargetBlock((HashSet<Byte>) null, 15);

        Location location = block.getLocation();
        PlacedSpawner placedSpawner = DataManager.getInstance().getPlacedSpawner(location);

        if (placedSpawner == null) {
            if (!spawnersShowing.containsKey(player)) return;

            List<PlacedSpawner> spawnersToHide = spawnersShowing.remove(player);

            for (PlacedSpawner spawner : spawnersToHide) {
                VoltzSpawners.get().getServer().getScheduler().runTaskLater(
                        VoltzSpawners.get(), () -> spawner.getHologram().hideHologramTo(player), 0L);

                for (Entity entity : spawner.getEntities()) {
                    VoltzSpawners.get().getServer().getScheduler().runTaskLater(
                            VoltzSpawners.get(), () -> EntityHider.getInstance().showEntity(player, entity), 0L);
                }
            }
            return;
        }

        SpawnerHologram hologram = placedSpawner.getHologram();
        if (hologram == null) return;

        VoltzSpawners.get().getServer().getScheduler().runTaskLater(
                VoltzSpawners.get(), () -> hologram.showHologramTo(player), 0L);

        for (Entity entity : placedSpawner.getEntities()) {
            VoltzSpawners.get().getServer().getScheduler().runTaskLater(
                    VoltzSpawners.get(), () -> EntityHider.getInstance().hideEntity(player, entity), 0L);
        }

        List<PlacedSpawner> spawners = spawnersShowing.containsKey(player) ? spawnersShowing.get(player) : new ArrayList<>(2);
        spawners.add(placedSpawner);

        spawnersShowing.put(player, spawners);
         */
    }
}
