package com.zpedroo.voltzspawners.listeners;

import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.InventoryManager;
import com.zpedroo.voltzspawners.objects.DropItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent event) {
        clearChunkEntities(event.getChunk());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkUnload(ChunkUnloadEvent event) {
        clearChunkEntities(event.getChunk());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickupDrop(PlayerPickupItemEvent event) {
        if (!event.getItem().hasMetadata("DropItem")) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        Item item = event.getItem();
        DropItem drop = (DropItem) item.getMetadata("DropItem").get(0).value();
        if (drop == null || drop.getStackAmount().signum() <= 0) return;

        ItemStack displayItem = item.getItemStack().clone();
        BigInteger freeSpace = BigInteger.valueOf(InventoryManager.getFreeSpace(player, displayItem));
        if (freeSpace.signum() <= 0) return;

        final BigInteger stackAmount = drop.getStackAmount();
        BigInteger amountToPickup = stackAmount.compareTo(freeSpace) > 0 ? freeSpace : stackAmount;
        ItemStack itemToGive = replaceItemPlaceholders(drop.getDrop().getItemToGive(), player);
        if (itemToGive != null) {
            itemToGive.setAmount(amountToPickup.intValue());
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

        BigInteger newStack = stackAmount.subtract(amountToPickup);
        drop.setStackAmount(newStack, item);
    }
    
    private void clearChunkEntities(Chunk chunk) {
        Arrays.stream(chunk.getEntities()).filter(entity -> entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.ITEM_FRAME && entity.getType() != EntityType.ARMOR_STAND && !entity.hasMetadata("***")).forEach(Entity::remove);
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
    }
}
