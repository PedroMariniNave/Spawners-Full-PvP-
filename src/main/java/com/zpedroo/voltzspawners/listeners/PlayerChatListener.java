package com.zpedroo.voltzspawners.listeners;

import com.zpedroo.multieconomy.api.CurrencyAPI;
import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.api.SpawnerBuyEvent;
import com.zpedroo.voltzspawners.enums.PlayerAction;
import com.zpedroo.voltzspawners.objects.PlayerChat;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.config.Messages;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
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
import java.util.HashMap;
import java.util.Map;

import static com.zpedroo.voltzspawners.utils.config.Messages.INSUFFICIENT_CURRENCY;
import static com.zpedroo.voltzspawners.utils.config.Messages.INVALID_AMOUNT;

public class PlayerChatListener implements Listener {

    private static final Map<Player, PlayerChat> playerChat = new HashMap<>(8);

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
                BigInteger spawnerPrice = playerChat.getPrice();
                Currency currency = playerChat.getCurrency();

                ItemStack itemToGive = null;

                BigInteger selectedAmount = null;
                if (StringUtils.equals(msg, "*")) {
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
                }
            }
        }.runTaskLater(VoltzSpawners.get(), 0L);
    }

    private void buy(Player player, ItemStack item, Currency currency, BigInteger price, int amount) {
        if (amount <= 0) {
            player.sendMessage(INVALID_AMOUNT);
            return;
        }

        BigInteger currencyAmount = CurrencyAPI.getCurrencyAmount(player, currency);

        if (currencyAmount.compareTo(price) < 0) {
            player.sendMessage(StringUtils.replaceEach(INSUFFICIENT_CURRENCY, new String[]{
                    "{has}",
                    "{need}"
            }, new String[]{
                    NumberFormatter.getInstance().format(currencyAmount),
                    NumberFormatter.getInstance().format(price)
            }));
            return;
        }

        CurrencyAPI.removeCurrencyAmount(player, currency, price);

        player.getInventory().addItem(item);

        for (String purchasedMsg : Messages.SUCCESSFUL_PURCHASED) {
            player.sendMessage(StringUtils.replaceEach(purchasedMsg, new String[]{
                    "{item}",
                    "{amount}",
                    "{price}"
            }, new String[]{
                    item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().toString(),
                    NumberFormatter.getInstance().format(BigInteger.valueOf(amount)),
                    StringUtils.replaceEach(currency.getAmountDisplay(), new String[]{ "{amount}" }, new String[]{ NumberFormatter.getInstance().format(price) })
            }));
        }

        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5f, 100f);
    }

    public static Map<Player, PlayerChat> getPlayerChat() {
        return playerChat;
    }
}