package com.zpedroo.voltzspawners.utils.menu;

import com.zpedroo.multieconomy.api.CurrencyAPI;
import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.voltzspawners.enums.PlayerAction;
import com.zpedroo.voltzspawners.listeners.PlayerChatListener;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.objects.Drop;
import com.zpedroo.voltzspawners.objects.PlayerChat;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.FileUtils;
import com.zpedroo.voltzspawners.utils.builder.InventoryBuilder;
import com.zpedroo.voltzspawners.utils.builder.InventoryUtils;
import com.zpedroo.voltzspawners.utils.builder.ItemBuilder;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.zpedroo.voltzspawners.utils.config.Messages.CHOOSE_AMOUNT;

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

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
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

    public void openTopSpawnersMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.TOP_SPAWNERS;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        String[] topSlots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");

        Map<UUID, BigInteger> topSpawners = DataManager.getInstance().getCache().getTopSpawners();
        int pos = 0;

        for (Map.Entry<UUID, BigInteger> entry : topSpawners.entrySet()) {
            UUID uuid = entry.getKey();
            BigInteger stack = entry.getValue();

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), "Item", new String[]{
                    "{player}",
                    "{spawners}",
                    "{pos}"
            }, new String[]{
                    Bukkit.getOfflinePlayer(uuid).getName(),
                    NumberFormatter.getInstance().format(stack),
                    String.valueOf(++pos)
            }).build();

            int slot = Integer.parseInt(topSlots[pos-1]);

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }

    public void openShopMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.SHOP;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
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

            Currency currency = CurrencyAPI.getCurrency(FileUtils.get().getString(file, "Inventory.items." + str + ".currency"));
            if (currency == null) currency = CurrencyAPI.getVaultCurrency();

            BigInteger price = NumberFormatter.getInstance().filter(FileUtils.get().getString(file, "Inventory.items." + str + ".price", "0"));
            String toGet = spawner.isUnlocked(player) ? "unlocked" : "locked";

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(),
                    FileUtils.get().getFile(file).getFileConfiguration().contains("Inventory.items." + str + ".locked")
                            && FileUtils.get().getFile(file).getFileConfiguration().contains("Inventory.items." + str + ".unlocked")
                            ? "Inventory.items." + str + "." + toGet
                            : "Inventory.items." + str, new String[]{
                            "{price}",
                            "{mcmmo}",
                            "{required_level}",
                            "{type}"
                    }, new String[]{
                            currency.getAmountDisplay(price),
                            NumberFormatter.getInstance().formatDecimal(spawner.getMcMMOExp()),
                            NumberFormatter.getInstance().formatDecimal(spawner.getRequiredLevel()),
                            spawner.getTypeTranslated()
                    }).build();
            int slot = Integer.parseInt(slots[i]);

            final Currency finalCurrency = currency;
            inventory.addItem(item, slot, () -> {
                if (price.signum() <= 0 || !spawner.isUnlocked(player)) return;

                player.closeInventory();
                PlayerChatListener.getPlayerChat().put(player, new PlayerChat(player, spawner, finalCurrency, price, PlayerAction.BUY_SPAWNER));
                clearChat(player);

                for (String msg : CHOOSE_AMOUNT) {
                    player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                            "{item}",
                            "{price}",
                            "{type}"
                    }, new String[]{
                            spawner.getItem(1).getItemMeta().getDisplayName(),
                            finalCurrency.getAmountDisplay(price),
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

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);
        String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");
        int i = -1;

        boolean onlyShowSingleDrops = FileUtils.get().getBoolean(file, "Inventory.only-show-single-drops");

        List<Drop> drops = spawner.getDrops();
        for (Drop drop : drops) {
            if (onlyShowSingleDrops && !drop.isSingleDrop()) continue;
            if (++i >= slots.length) i = 0;

            ItemStack displayItem = drop.getDisplayItem();
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