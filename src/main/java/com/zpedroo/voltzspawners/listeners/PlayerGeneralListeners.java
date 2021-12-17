package com.zpedroo.voltzspawners.listeners;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.EntityManager;
import com.zpedroo.voltzspawners.managers.InventoryManager;
import com.zpedroo.voltzspawners.objects.Drop;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.config.Titles;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import de.tr7zw.nbtapi.NBTItem;
import net.minecraft.server.v1_8_R3.BiomeBase;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.Arrays;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkLoad(ChunkLoadEvent event) {
        for (BlockState blockState : event.getChunk().getTileEntities()) {
            Block block = blockState.getBlock();
            PlacedSpawner placedSpawner = DataManager.getInstance().getPlacedSpawner(block.getLocation());
            if (placedSpawner == null) return;

            placedSpawner.load();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkUnload(ChunkUnloadEvent event) {
        for (BlockState blockState : event.getChunk().getTileEntities()) {
            Block block = blockState.getBlock();
            PlacedSpawner placedSpawner = DataManager.getInstance().getPlacedSpawner(block.getLocation());
            if (placedSpawner == null) return;

            placedSpawner.unload();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;

        Player player = event.getPlayer();
        final BigInteger[] finalPrice = { BigInteger.ZERO };

        Arrays.stream(player.getInventory().getContents()).filter(itemStack ->
                itemStack != null && itemStack.getType() != Material.AIR).forEach(item -> {
            NBTItem nbt = new NBTItem(item);
            if (!nbt.hasKey("DropSpawner")) return;

            Spawner spawner = DataManager.getInstance().getSpawner(nbt.getString("DropSpawner"));
            if (spawner == null) return;

            finalPrice[0] = finalPrice[0].add(spawner.getDropsValue().multiply(BigInteger.valueOf(item.getAmount())));
            player.getInventory().removeItem(item);
        });

        if (finalPrice[0].signum() <= 0) return;

        player.sendTitle(Titles.WHEN_SELL_TITLE, StringUtils.replaceEach(Titles.WHEN_SELL_SUBTITLE, new String[]{
                "{final_price}"
        }, new String[]{
                NumberFormatter.getInstance().format(finalPrice[0])
        }));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickupDrop(PlayerPickupItemEvent event) {
        if (!event.getItem().hasMetadata("DropID")) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        int dropID = event.getItem().getMetadata("DropID").get(0).asInt();
        Drop drop = DataManager.getInstance().getCache().getDrops().get(dropID);
        if (drop == null) return;

        NBTItem nbt = new NBTItem(drop.getItemStack());
        nbt.setString("DropSpawner", drop.getSpawner().getSpawner().getType());

        ItemStack item = nbt.getItem().clone();
        int freeSpace = InventoryManager.getFreeSpace(player, item);
        if (freeSpace <= 0) return;

        BigInteger stackAmount = drop.getStackAmount();
        if (BigInteger.valueOf(freeSpace).compareTo(stackAmount) > 0) freeSpace = stackAmount.intValue();

        drop.setStackAmount(stackAmount.subtract(BigInteger.valueOf(freeSpace)));

        item.setAmount(freeSpace);
        player.getInventory().addItem(item);
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);
    }
}