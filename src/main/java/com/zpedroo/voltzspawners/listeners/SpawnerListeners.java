package com.zpedroo.voltzspawners.listeners;

import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.InventoryManager;
import com.zpedroo.voltzspawners.managers.SpawnerManager;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.checker.PlotChecker;
import com.zpedroo.voltzspawners.utils.config.Messages;
import com.zpedroo.voltzspawners.utils.config.Settings;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;

public class SpawnerListeners implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getItemInHand() == null || event.getItemInHand().getType().equals(Material.AIR)) return;

        ItemStack item = event.getItemInHand().clone();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("SpawnersType")) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        if (!player.hasPermission("spawner.admin") && !PlotChecker.canInteractInPlot(player, block.getLocation())) return;

        Spawner spawner = DataManager.getInstance().getSpawner(nbt.getString("SpawnersType").toUpperCase());
        if (spawner == null) return;
        if (!spawner.isUnlocked(player)) {
            player.sendMessage(Messages.LOCKED_SPAWNER);
            return;
        }

        BigInteger stackAmount = player.isSneaking() ? BigInteger.valueOf(item.getAmount()) : BigInteger.ONE;
        Object[] objects = SpawnerManager.getInstance().getNearSpawners(player, block, stackAmount, spawner.getType());
        PlacedSpawner placedSpawner = objects != null ? (PlacedSpawner) objects[0] : null;
        BigInteger overLimit = null;
        if (placedSpawner != null) {
            overLimit = (BigInteger)objects[1];
            placedSpawner.addStack(stackAmount.subtract(overLimit));
        } else {
            int radius = Settings.STACK_RADIUS;
            if (radius > 0) {
                for (int x = -radius; x <= radius; ++x) {
                    for (int y = -radius; y <= radius; ++y) {
                        for (int z = -radius; z <= radius; ++z) {
                            Block blocks = block.getRelative(x, y, z);
                            if (!blocks.getType().equals(Material.AIR)) {
                                placedSpawner = DataManager.getInstance().getPlacedSpawner(blocks.getLocation());
                                if (placedSpawner != null) {
                                    event.setCancelled(true);
                                    player.sendMessage(Messages.NEAR_SPAWNER);
                                    return;
                                }
                            }
                        }
                    }
                }
            }

            if (spawner.getMaximumStack().signum() > 0 && stackAmount.compareTo(spawner.getMaximumStack()) > 0) {
                overLimit = stackAmount.subtract(spawner.getMaximumStack());
            } else {
                overLimit = BigInteger.ZERO;
            }

            placedSpawner = new PlacedSpawner(block.getLocation(), player.getUniqueId(), stackAmount.subtract(overLimit), spawner);
            placedSpawner.load();
            placedSpawner.cache();
            placedSpawner.setUpdate(true);
        }

        item.setAmount(stackAmount.intValue());
        player.getInventory().removeItem(item);
        if (overLimit.compareTo(BigInteger.ZERO) >= 1) {
            player.getInventory().addItem(spawner.getItem(overLimit.intValue()));
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        PlacedSpawner spawner = DataManager.getInstance().getPlacedSpawner(block.getLocation());
        if (spawner == null) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (!spawner.canInteract(player)) {
            player.sendMessage(Messages.NEED_PERMISSION);
            return;
        }

        ItemStack itemInHand = event.getPlayer().getItemInHand().clone();
        boolean silkTouchPickaxe = itemInHand.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0 && StringUtils.endsWith(itemInHand.getType().toString(), "_PICKAXE");
        if (!silkTouchPickaxe) {
            player.sendMessage(Messages.INCORRECT_PICKAXE);
            return;
        }

        spawner.delete();

        int stackAmount = spawner.getStack().intValue();
        ItemStack item = spawner.getSpawner().getItem(stackAmount);
        int freeSpace = InventoryManager.getFreeSpace(player, item);

        item.setAmount(stackAmount);
        player.getInventory().addItem(item);

        if (freeSpace < stackAmount) {
            item.setAmount(stackAmount - freeSpace);
            spawner.getLocation().getWorld().dropItemNaturally(spawner.getLocation(), item);
        }
    }
}
