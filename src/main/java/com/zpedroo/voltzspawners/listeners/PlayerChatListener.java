package com.zpedroo.voltzspawners.listeners;

import com.zpedroo.multieconomy.api.CurrencyAPI;
import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.enums.PlayerAction;
import com.zpedroo.voltzspawners.objects.PlayerChat;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.config.Messages;
import com.zpedroo.voltzspawners.utils.config.Settings;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class PlayerChatListener implements Listener {

    private static final Map<Player, PlayerChat> playerChat = new HashMap<>(4);
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!playerChat.containsKey(event.getPlayer())) return;

        event.setCancelled(true);

        new BukkitRunnable() {
            public void run() {
                PlayerChat playerChat = PlayerChatListener.getPlayerChat().remove(event.getPlayer());
                Player player = playerChat.getPlayer();
                String msg = event.getMessage();
                PlayerAction playerAction = playerChat.getAction();
                Map<Currency, BigInteger> prices = playerChat.getPrices();
                ItemStack itemToGive = null;
                BigInteger selectedAmount = BigInteger.ZERO;
                if (StringUtils.equals(msg, "*")) {
                    for (Map.Entry<Currency, BigInteger> entry : prices.entrySet()) {
                        Currency currency = entry.getKey();
                        BigInteger price = entry.getValue();

                        selectedAmount = selectedAmount.add(CurrencyAPI.getCurrencyAmount(player, currency).divide(price));
                    }
                } else {
                    selectedAmount = NumberFormatter.getInstance().filter(msg);
                }

                if (selectedAmount.compareTo(BigInteger.valueOf(2304L)) > 0) selectedAmount = BigInteger.valueOf(2304L);

                switch (playerAction) {
                    case BUY_SPAWNER: {
                        Spawner spawner = playerChat.getSpawner();
                        int finalAmount = selectedAmount.intValue();
                        itemToGive = spawner.getItem(finalAmount);

                        buy(player, itemToGive, prices, finalAmount);
                        break;
                    }
                }
            }
        }.runTaskLater(VoltzSpawners.get(), 0L);
    }
    
    private void buy(Player player, ItemStack item, Map<Currency, BigInteger> prices, int amount) {
        if (amount <= 0) {
            player.sendMessage(Messages.INVALID_AMOUNT);
            return;
        }

        if (!hasCurrenciesAmount(player, prices)) return;

        removeCurrencies(player, prices);
        player.getInventory().addItem(item);
        for (String msg : Messages.SUCCESSFUL_PURCHASED) {
            player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                    "{item}",
                    "{amount}",
                    "{price}"
            }, new String[]{
                    item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().toString(),
                    NumberFormatter.getInstance().format(BigInteger.valueOf(amount)),
                    getCurrenciesDisplay(prices)
            }));
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5f, 100f);
        }
    }

    private String getCurrenciesDisplay(Map<Currency, BigInteger> currencies) {
        StringBuilder ret = new StringBuilder();
        for (Map.Entry<Currency, BigInteger> entry : currencies.entrySet()) {
            Currency currency = entry.getKey();
            BigInteger value = entry.getValue();

            if (ret.length() > 0) ret.append(Settings.CURRENCY_SEPARATOR);
            ret.append(currency.getAmountDisplay(value));
        }

        return ret.toString();
    }

    private void removeCurrencies(Player player, Map<Currency, BigInteger> prices) {
        for (Map.Entry<Currency, BigInteger> entry : prices.entrySet()) {
            Currency currency = entry.getKey();
            BigInteger value = entry.getValue();

            CurrencyAPI.removeCurrencyAmount(player, currency, value);
        }
    }

    private boolean hasCurrenciesAmount(Player player, Map<Currency, BigInteger> prices) {
        for (Map.Entry<Currency, BigInteger> entry : prices.entrySet()) {
            Currency currency = entry.getKey();
            BigInteger value = entry.getValue();

            BigInteger currencyAmount = CurrencyAPI.getCurrencyAmount(player, currency);
            if (currencyAmount.compareTo(value) < 0) {
                player.sendMessage(StringUtils.replaceEach(Messages.INSUFFICIENT_CURRENCY, new String[]{
                        "{has}",
                        "{need}"
                }, new String[]{
                        NumberFormatter.getInstance().format(currencyAmount),
                        NumberFormatter.getInstance().format(value)
                }));
                return false;
            }
        }

        return true;
    }

    public static Map<Player, PlayerChat> getPlayerChat() {
        return playerChat;
    }
}
