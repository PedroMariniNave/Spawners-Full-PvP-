package com.zpedroo.voltzspawners.listeners;

import com.zpedroo.multieconomy.api.CurrencyAPI;
<<<<<<< HEAD
import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.api.SpawnerBuyEvent;
import com.zpedroo.voltzspawners.enums.PlayerAction;
import com.zpedroo.voltzspawners.objects.PlayerChat;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.config.Messages;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
=======
import com.zpedroo.multieconomy.objects.Currency;
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.objects.Manager;
import com.zpedroo.voltzspawners.objects.PlayerChat;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.utils.config.Messages;
import com.zpedroo.voltzspawners.utils.enums.PlayerAction;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import com.zpedroo.voltzspawners.utils.menu.Menus;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigInteger;
<<<<<<< HEAD
import java.util.HashMap;
import java.util.Map;

import static com.zpedroo.voltzspawners.utils.config.Messages.INSUFFICIENT_CURRENCY;
import static com.zpedroo.voltzspawners.utils.config.Messages.INVALID_AMOUNT;

public class PlayerChatListener implements Listener {

    private static final Map<Player, PlayerChat> playerChat = new HashMap<>(8);
=======
import java.util.*;

import static com.zpedroo.voltzspawners.utils.config.Messages.*;
import static com.zpedroo.voltzspawners.utils.config.Settings.*;

public class PlayerChatListener implements Listener {

    private static Map<Player, PlayerChat> playerChat;

    static {
        playerChat = new HashMap<>(16);
    }
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!playerChat.containsKey(event.getPlayer())) return;

        event.setCancelled(true);

        // sync method
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerChat playerChat = getPlayerChat().remove(event.getPlayer());
                Player player = playerChat.getPlayer();
                String msg = event.getMessage();
                PlayerAction playerAction = playerChat.getAction();
<<<<<<< HEAD
                BigInteger spawnerPrice = playerChat.getPrice();
=======

                PlacedSpawner placedSpawner = playerChat.getPlacedSpawner();
                BigInteger price = playerChat.getPrice();
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
                Currency currency = playerChat.getCurrency();

                ItemStack itemToGive = null;

                BigInteger selectedAmount = null;
                if (StringUtils.equals(msg, "*")) {
<<<<<<< HEAD
                    selectedAmount = CurrencyAPI.getCurrencyAmount(player, currency).divide(spawnerPrice);
                } else {
                    selectedAmount = NumberFormatter.getInstance().filter(msg);
                }

                if (selectedAmount.compareTo(BigInteger.valueOf(2304)) > 0) selectedAmount = BigInteger.valueOf(2304);

                switch (playerAction) {
                    case BUY_SPAWNER:
                        Spawner spawner = playerChat.getSpawner();
                        BigInteger price = spawnerPrice.multiply(selectedAmount);

                        SpawnerBuyEvent spawnerBuyEvent = new SpawnerBuyEvent(player, spawner, selectedAmount, price);
                        Bukkit.getPluginManager().callEvent(spawnerBuyEvent);
                        if (spawnerBuyEvent.isCancelled()) return;

                        int finalAmount = spawnerBuyEvent.getAmount().intValue();
                        BigInteger finalPrice = spawnerBuyEvent.getPrice();
                        itemToGive = spawner.getItem(finalAmount);
                        buy(player, itemToGive, currency, finalPrice, finalAmount);
=======
                    selectedAmount = CurrencyAPI.getCurrencyAmount(player, currency).divide(price);
                } else {
                    selectedAmount = NumberFormatter.getInstance().filter(msg);
                }
                switch (playerAction) {
                    case BUY_SPAWNER:
                        Spawner spawner = playerChat.getSpawner();
                        itemToGive = spawner.getItem(selectedAmount);
                        buy(player, itemToGive, currency, price, selectedAmount);
                        return;
                    case ADD_FRIEND:
                        Player target = Bukkit.getPlayer(msg);
                        player.sendMessage(WAIT);
                        if (target == null) {
                            player.sendMessage(OFFLINE_PLAYER);
                            return;
                        }

                        Manager manager = placedSpawner.getManager(target.getUniqueId());
                        if (placedSpawner.getOwnerUUID().equals(target.getUniqueId()) || manager != null) {
                            player.sendMessage(HAS_PERMISSION);
                            return;
                        }

                        placedSpawner.getManagers().add(new Manager(target.getUniqueId(), new ArrayList<>(5)));
                        placedSpawner.setUpdate(true);
                        Menus.getInstance().openManagersMenu(player, placedSpawner);
                        return;
                    case REMOVE_STACK:
                        BigInteger stack = placedSpawner.getStack();
                        BigInteger tax = BigInteger.valueOf(TAX_REMOVE_STACK);
                        if (StringUtils.equals(msg, "*")) {
                            if (stack.compareTo(tax) < 0) {
                                player.sendMessage(StringUtils.replaceEach(REMOVE_STACK_MIN, new String[]{
                                        "{tax}"
                                }, new String[]{
                                        String.valueOf(tax)
                                }));
                                return;
                            }

                            BigInteger toGive = stack.subtract(stack.multiply(tax).divide(BigInteger.valueOf(100)));

                            placedSpawner.removeStack(stack);
                            player.getInventory().addItem(placedSpawner.getSpawner().getItem(toGive));
                            player.sendMessage(StringUtils.replaceEach(REMOVE_STACK_SUCCESSFUL, new String[]{
                                    "{lost}"
                            }, new String[]{
                                    NumberFormatter.getInstance().format(stack.subtract(toGive))
                            }));
                            return;
                        }

                        BigInteger toRemove = NumberFormatter.getInstance().filter(msg);
                        if (toRemove.signum() <= 0) {
                            player.sendMessage(INVALID_AMOUNT);
                            return;
                        }

                        if (toRemove.compareTo(stack) > 0) toRemove = stack;
                        if (toRemove.compareTo(tax) < 0) {
                            player.sendMessage(StringUtils.replaceEach(REMOVE_STACK_MIN, new String[]{
                                    "{tax}"
                            }, new String[]{
                                    String.valueOf(tax)
                            }));
                            return;
                        }

                        BigInteger toGive = toRemove.subtract(toRemove.multiply(tax).divide(BigInteger.valueOf(100)));
                        placedSpawner.removeStack(toRemove);
                        player.getInventory().addItem(placedSpawner.getSpawner().getItem(toGive));
                        player.sendMessage(StringUtils.replaceEach(REMOVE_STACK_SUCCESSFUL, new String[]{
                                "{lost}"
                        }, new String[]{
                                NumberFormatter.getInstance().format(toRemove.subtract(toGive))
                        }));
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
                }
            }
        }.runTaskLater(VoltzSpawners.get(), 0L);
    }

<<<<<<< HEAD
    private void buy(Player player, ItemStack item, Currency currency, BigInteger price, int amount) {
        if (amount <= 0) {
=======
    private void buy(Player player, ItemStack item, Currency currency, BigInteger price, BigInteger amount) {
        if (amount.signum() <= 0) {
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
            player.sendMessage(INVALID_AMOUNT);
            return;
        }

        BigInteger currencyAmount = CurrencyAPI.getCurrencyAmount(player, currency);
<<<<<<< HEAD

        if (currencyAmount.compareTo(price) < 0) {
=======
        BigInteger finalPrice = price.multiply(amount);

        if (currencyAmount.compareTo(finalPrice) < 0) {
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
            player.sendMessage(StringUtils.replaceEach(INSUFFICIENT_CURRENCY, new String[]{
                    "{has}",
                    "{need}"
            }, new String[]{
                    NumberFormatter.getInstance().format(currencyAmount),
<<<<<<< HEAD
                    NumberFormatter.getInstance().format(price)
=======
                    NumberFormatter.getInstance().format(finalPrice)
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
            }));
            return;
        }

<<<<<<< HEAD
        CurrencyAPI.removeCurrencyAmount(player, currency, price);
=======
        CurrencyAPI.removeCurrencyAmount(player, currency, finalPrice);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

        player.getInventory().addItem(item);

        for (String purchasedMsg : Messages.SUCCESSFUL_PURCHASED) {
            player.sendMessage(StringUtils.replaceEach(purchasedMsg, new String[]{
                    "{item}",
                    "{amount}",
                    "{price}"
            }, new String[]{
                    item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().toString(),
<<<<<<< HEAD
                    NumberFormatter.getInstance().format(BigInteger.valueOf(amount)),
                    StringUtils.replaceEach(currency.getAmountDisplay(), new String[]{ "{amount}" }, new String[]{ NumberFormatter.getInstance().format(price) })
=======
                    NumberFormatter.getInstance().format(amount),
                    StringUtils.replaceEach(currency.getAmountDisplay(), new String[]{ "{amount}" }, new String[]{ NumberFormatter.getInstance().format(finalPrice) })
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
            }));
        }

        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5f, 100f);
    }

    public static Map<Player, PlayerChat> getPlayerChat() {
        return playerChat;
    }
}