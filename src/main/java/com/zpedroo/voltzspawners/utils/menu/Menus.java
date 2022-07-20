package com.zpedroo.voltzspawners.utils.menu;

import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.voltzspawners.utils.color.Colorize;
import org.bukkit.inventory.*;
import com.zpedroo.voltzspawners.utils.*;
import org.bukkit.entity.*;
import com.zpedroo.voltzspawners.utils.builder.*;
import com.zpedroo.voltzspawners.managers.*;
import java.math.*;
import com.zpedroo.voltzspawners.utils.formatter.*;
import com.zpedroo.multieconomy.api.*;
import com.zpedroo.voltzspawners.listeners.*;
import com.zpedroo.voltzspawners.enums.*;
import com.zpedroo.voltzspawners.utils.config.*;
import org.apache.commons.lang.*;
import com.zpedroo.voltzspawners.objects.*;
import java.util.*;
import org.bukkit.*;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    private final ItemStack nextPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).getFileConfiguration(), "Next-Page").build();
    private final ItemStack previousPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).getFileConfiguration(), "Previous-Page").build();
    
    public Menus() {
        instance = this;
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = Colorize.getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        for (String items : FileUtils.get().getSection(file, "Inventory.items")) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), "Inventory.items." + items).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + items + ".slot");
            String action = FileUtils.get().getString(file, "Inventory.items." + items + ".action");

            inventory.addItem(item, slot, () -> {
                switch (action.toUpperCase()) {
                    case "SHOP":
                        openShopMenu(player);
                        player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
                        break;
                    case "TOP":
                        openTopSpawnersMenu(player);
                        player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
                        break;
                }
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }
    
    public void openTopSpawnersMenu(final Player player) {
        FileUtils.Files file = FileUtils.Files.TOP_SPAWNERS;

        String title = Colorize.getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);
        String[] topSlots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");
        Map<UUID, BigInteger> topSpawners = DataManager.getInstance().getCache().getTopSpawners();
        int pos = 0;

        for (Map.Entry<UUID, BigInteger> entry : topSpawners.entrySet()) {
            UUID uuid = entry.getKey();
            BigInteger stack = entry.getValue();
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), "Item", new String[] { "{player}", "{spawners}", "{pos}" }, new String[] { Bukkit.getOfflinePlayer(uuid).getName(), NumberFormatter.getInstance().format(stack), String.valueOf(++pos) }).build();
            int slot = Integer.parseInt(topSlots[pos - 1]);

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }
    
    public void openShopMenu(final Player player) {
        FileUtils.Files file = FileUtils.Files.SHOP;

        String title = Colorize.getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");
        int nextPageSlot = FileUtils.get().getInt(file, "Inventory.next-page-slot");
        int previousPageSlot = FileUtils.get().getInt(file, "Inventory.previous-page-slot");

        InventoryBuilder inventory = new InventoryBuilder(title, size, previousPageItem, previousPageSlot, nextPageItem, nextPageSlot);
        String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");
        int i = -1;
        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            if (++i >= slots.length) i = 0;

            Spawner spawner = DataManager.getInstance().getSpawner(str);
            if (spawner == null) continue;

            String where = "Inventory.items." + str;
            Map<Currency, BigInteger> price = new HashMap<>(2);
            StringBuilder priceDisplay = new StringBuilder();
            for (String prices : FileUtils.get().getStringList(file, where + ".price")) {
                String[] split = prices.split(",");
                if (split.length < 2) continue;

                Currency currency = CurrencyAPI.getCurrency(split[0]);
                if (currency == null) continue;

                BigInteger value = NumberFormatter.getInstance().filter(split[1]);
                if (value.signum() <= 0) continue;

                price.put(currency, value);

                if (priceDisplay.length() > 0) priceDisplay.append(Settings.CURRENCY_SEPARATOR);
                priceDisplay.append(currency.getAmountDisplay(value));
            }

            String toGet = spawner.isUnlocked(player) ? "unlocked" : "locked";
            ItemStack item = null;
            String[] placeholders = new String[]{ "{price}", "{mcmmo}", "{required_level}", "{type}" };
            String[] replacers = new String[]{
                    priceDisplay.toString(),
                    NumberFormatter.getInstance().formatDecimal(spawner.getMcMMOExp()),
                    NumberFormatter.getInstance().formatDecimal(spawner.getRequiredLevel()),
                    spawner.getTypeTranslated()
            };
            if (FileUtils.get().getFile(file).getFileConfiguration().contains(where + ".locked") && FileUtils.get().getFile(file).getFileConfiguration().contains(where + ".unlocked")) {
                item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), where + "." + toGet, placeholders, replacers).build();
            } else {
                item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), where, placeholders, replacers).build();
            }
            int slot = Integer.parseInt(slots[i]);

            inventory.addItem(item, slot, () -> {
                if (!spawner.isUnlocked(player)) return;

                inventory.close(player);
                PlayerChatListener.getPlayerChat().put(player, new PlayerChat(player, spawner, price, PlayerAction.BUY_SPAWNER));
                clearChat(player);

                for (String msg : Messages.CHOOSE_AMOUNT) {
                    player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                            "{item}",
                            "{price}",
                            "{type}"
                    }, new String[]{
                            spawner.getItem(1).getItemMeta().getDisplayName(),
                            priceDisplay.toString(),
                            spawner.getTypeTranslated()
                    }));
                }
            }, ActionType.LEFT_CLICK);

            inventory.addAction(slot, () -> {
                if (!spawner.hasSpecialDrops()) return;

                openDropsPreviewMenu(player, spawner);
            }, ActionType.RIGHT_CLICK);
        }

        inventory.open(player);
    }
    
    public void openDropsPreviewMenu(Player player, Spawner spawner) {
        FileUtils.Files file = FileUtils.Files.DROPS_PREVIEW;

        String title = Colorize.getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);
        String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");
        int i = -1;
        boolean onlyShowSingleDrops = FileUtils.get().getBoolean(file, "Inventory.only-show-single-drops");
        List<SpawnerDrop> spawnerDrops = spawner.getDrops();
        for (SpawnerDrop spawnerDrop : spawnerDrops) {
            if (onlyShowSingleDrops && !spawnerDrop.isSingleDrop()) continue;
            if (++i >= slots.length) i = 0;

            ItemStack displayItem = spawnerDrop.getDisplayItem();
            int slot = Integer.parseInt(slots[i]);

            inventory.addItem(displayItem, slot);
        }

        ItemStack backItem = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), "Back-Item").build();
        int backSlot = FileUtils.get().getInt(file, "Back-Item.slot");

        inventory.addItem(backItem, backSlot, () -> {
            openShopMenu(player);
        }, ActionType.ALL_CLICKS);

        inventory.open(player);
    }
    
    private void clearChat(Player player) {
        for (int i = 0; i < 25; ++i) {
            player.sendMessage("");
        }
    }
}