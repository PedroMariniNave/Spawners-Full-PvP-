package com.zpedroo.voltzspawners.listeners;

<<<<<<< HEAD
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.InventoryManager;
import com.zpedroo.voltzspawners.objects.DropItem;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
=======
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
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
<<<<<<< HEAD
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
=======
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
<<<<<<< HEAD
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        for (BlockState blockState : event.getChunk().getTileEntities()) {
            if (blockState.getBlock().getType() != Material.MOB_SPAWNER) continue;

            Block block = blockState.getBlock();
            PlacedSpawner placedSpawner = DataManager.getInstance().getPlacedSpawner(block.getLocation());
            if (placedSpawner != null) placedSpawner.load();
        }

        clearChunkEntities(event.getChunk());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        /*
        for (BlockState blockState : event.getChunk().getTileEntities()) {
            if (blockState.getBlock().getType() != Material.MOB_SPAWNER) continue;

            Block block = blockState.getBlock();
            PlacedSpawner placedSpawner = DataManager.getInstance().getPlacedSpawner(block.getLocation());
            if (placedSpawner != null) placedSpawner.unload();
        }
         */

        clearChunkEntities(event.getChunk());
=======
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
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickupDrop(PlayerPickupItemEvent event) {
        if (!event.getItem().hasMetadata("DropID")) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
<<<<<<< HEAD
        long dropID = event.getItem().getMetadata("DropID").get(0).asLong();
        DropItem drop = DataManager.getInstance().getCache().getDrops().get(dropID);
        if (drop == null) return;

        ItemStack displayItem = event.getItem().getItemStack().clone();
        int freeSpace = InventoryManager.getFreeSpace(player, displayItem);
=======
        int dropID = event.getItem().getMetadata("DropID").get(0).asInt();
        Drop drop = DataManager.getInstance().getCache().getDrops().get(dropID);
        if (drop == null) return;

        NBTItem nbt = new NBTItem(drop.getItemStack());
        nbt.setString("DropSpawner", drop.getSpawner().getSpawner().getType());

        ItemStack item = nbt.getItem().clone();
        int freeSpace = InventoryManager.getFreeSpace(player, item);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        if (freeSpace <= 0) return;

        BigInteger stackAmount = drop.getStackAmount();
        if (BigInteger.valueOf(freeSpace).compareTo(stackAmount) > 0) freeSpace = stackAmount.intValue();

<<<<<<< HEAD
        ItemStack itemToGive = replaceItemPlaceholders(drop.getDrop().getItemToGive(), player);
        if (itemToGive != null) {
            itemToGive.setAmount(freeSpace);
            player.getInventory().addItem(itemToGive);
        }

        for (String command : drop.getDrop().getCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replaceEach(command, new String[]{
                    "{player}"
            }, new String[]{
                    player.getName()
            }));
        }

        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);
        drop.setStackAmount(stackAmount.subtract(BigInteger.valueOf(freeSpace)));
    }

    private void clearChunkEntities(Chunk chunk) {
        Arrays.stream(chunk.getEntities()).filter(entity -> entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.ITEM_FRAME
                && entity.getType() != EntityType.ARMOR_STAND && !entity.hasMetadata("***")
        ).forEach(Entity::remove);
    }

    private ItemStack replaceItemPlaceholders(ItemStack item, Player player) {
        if (item == null) return null;

        ItemStack itemClone = item.clone();
        ItemMeta meta = itemClone.getItemMeta();
        if (meta != null) {
            if (meta.hasDisplayName()) {
                meta.setDisplayName(StringUtils.replaceEach(meta.getDisplayName(), new String[]{
                        "{player}"
                }, new String[]{
                        player.getName()
                }));
            }

            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                List<String> newLore = new ArrayList<>(lore.size());
                for (String lines : lore) {
                    newLore.add(StringUtils.replaceEach(lines, new String[]{
                            "{player}"
                    }, new String[]{
                            player.getName()
                    }));
                }

                meta.setLore(newLore);
            }

            itemClone.setItemMeta(meta);
        }

        return itemClone;
=======
        drop.setStackAmount(stackAmount.subtract(BigInteger.valueOf(freeSpace)));

        item.setAmount(freeSpace);
        player.getInventory().addItem(item);
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }
}