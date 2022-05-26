package com.zpedroo.voltzspawners.listeners;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
<<<<<<< HEAD
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
=======
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.SpawnerManager;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.config.Messages;
import com.zpedroo.voltzspawners.utils.config.Settings;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import com.zpedroo.voltzspawners.utils.menu.Menus;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
<<<<<<< HEAD
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
=======
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigInteger;
import java.util.ArrayList;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

public class SpawnerListeners implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getItemInHand() == null || event.getItemInHand().getType().equals(Material.AIR)) return;

        ItemStack item = event.getItemInHand().clone();
        NBTItem nbt = new NBTItem(item);
<<<<<<< HEAD
        if (!nbt.hasKey("SpawnersType")) return;

        event.setCancelled(true);
=======
        if (!nbt.hasKey("SpawnersAmount")) return;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

<<<<<<< HEAD
        if (!player.hasPermission("spawner.admin") && !PlotChecker.canInteractInPlot(player, block.getLocation())) return;

        Spawner spawner = DataManager.getInstance().getSpawner(nbt.getString("SpawnersType").toUpperCase());
        if (spawner == null) return;
        if (!spawner.isUnlocked(player)) {
            player.sendMessage(Messages.LOCKED_SPAWNER);
            return;
        }

        BigInteger stackAmount = player.isSneaking() ? BigInteger.valueOf(item.getAmount()) : BigInteger.ONE;
        Object[] objects = SpawnerManager.getInstance().getNearSpawners(player, block, stackAmount, spawner.getType());
=======
        if (!player.hasPermission("spawner.admin")) {
            Location location = new Location(
                    block.getWorld().getName(),
                    block.getX(),
                    block.getY(),
                    block.getZ()
            );
            Plot plot = Plot.getPlot(location);
            if (plot == null) return;
            if (!plot.isAdded(player.getUniqueId())) return;
        }

        Spawner spawner = DataManager.getInstance().getSpawner(nbt.getString("SpawnersType").toUpperCase());
        if (spawner == null) return;
        if (spawner.getPermission() != null && !player.hasPermission(spawner.getPermission())) {
            event.setCancelled(true);
            player.sendMessage(Messages.SPAWNER_PERMISSION);
            return;
        }

        BigInteger stack = new BigInteger(nbt.getString("SpawnersAmount"));
        Object[] objects = SpawnerManager.getInstance().getNearSpawners(player, block, stack, spawner.getType());
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        PlacedSpawner placedSpawner = objects != null ? (PlacedSpawner) objects[0] : null;
        BigInteger overLimit = null;

        if (placedSpawner != null) {
<<<<<<< HEAD
            overLimit = (BigInteger) objects[1];
            placedSpawner.addStack(stackAmount.subtract(overLimit));
=======
            event.setCancelled(true);

            overLimit = (BigInteger) objects[1];
            placedSpawner.addStack(stack.subtract(overLimit));
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        } else {
            int radius = Settings.STACK_RADIUS;

            if (radius > 0) {
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block blocks = block.getRelative(x, y, z);
                            if (blocks.getType().equals(Material.AIR)) continue;

                            placedSpawner = DataManager.getInstance().getPlacedSpawner(blocks.getLocation());
                            if (placedSpawner == null) continue;

                            event.setCancelled(true);
                            player.sendMessage(Messages.NEAR_SPAWNER);
                            return;
                        }
                    }
                }
            }

<<<<<<< HEAD
            if (spawner.getMaximumStack().signum() > 0 && stackAmount.compareTo(spawner.getMaximumStack()) > 0) {
                overLimit = stackAmount.subtract(spawner.getMaximumStack());
            } else overLimit = BigInteger.ZERO;

            placedSpawner = new PlacedSpawner(block.getLocation(), player.getUniqueId(), stackAmount.subtract(overLimit), spawner);
=======
            if (spawner.getMaximumStack().signum() > 0 && stack.compareTo(spawner.getMaximumStack()) > 0) {
                overLimit = stack.subtract(spawner.getMaximumStack());
            } else overLimit = BigInteger.ZERO;

            placedSpawner = new PlacedSpawner(block.getLocation(), player.getUniqueId(), stack.subtract(overLimit), spawner, new ArrayList<>(2), false);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
            placedSpawner.load();
            placedSpawner.cache();
            placedSpawner.setUpdate(true);
        }

<<<<<<< HEAD
        item.setAmount(stackAmount.intValue());
        player.getInventory().removeItem(item);

        if (overLimit.compareTo(BigInteger.ZERO) >= 1) player.getInventory().addItem(spawner.getItem(overLimit.intValue()));
=======
        item.setAmount(1);
        player.getInventory().removeItem(item);

        if (overLimit.compareTo(BigInteger.ZERO) >= 1) player.getInventory().addItem(spawner.getItem(overLimit));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpawnerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType().equals(Material.AIR)) return;

        PlacedSpawner spawner = DataManager.getInstance().getPlacedSpawner(block.getLocation());
        if (spawner == null) return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        if (!spawner.canInteract(player)) {
            player.sendMessage(Messages.NEED_PERMISSION);
            return;
        }

        Menus.getInstance().openSpawnerMenu(player, spawner);
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.5f, 2f);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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

<<<<<<< HEAD
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
=======
        spawner.delete();

        ItemStack itemInHand = event.getPlayer().getItemInHand().clone();
        BigInteger toGive = spawner.getStack();

        boolean silkTouch = false;

        if (StringUtils.endsWith(itemInHand.getType().toString(), "_PICKAXE")) {
            if (itemInHand.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0) silkTouch = true;
        }

        if (!silkTouch) {
            toGive = spawner.getStack().subtract(spawner.getStack().multiply(BigInteger.valueOf(Settings.TAX_REMOVE_STACK)).divide(BigInteger.valueOf(100)));
        }

        if (toGive.compareTo(spawner.getStack()) < 0) {
            player.sendMessage(StringUtils.replaceEach(Messages.INCORRECT_PICKAXE, new String[]{
                    "{lost}"
            }, new String[]{
                    NumberFormatter.getInstance().format(spawner.getStack().subtract(toGive))
            }));
        }

        player.getInventory().addItem(spawner.getSpawner().getItem(toGive));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDespawn(ItemDespawnEvent event) {
        if (!event.getEntity().hasMetadata("***")) return;

        event.setCancelled(true);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }
}