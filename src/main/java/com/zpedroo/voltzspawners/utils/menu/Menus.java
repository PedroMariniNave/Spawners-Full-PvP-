package com.zpedroo.voltzspawners.utils.menu;

import com.zpedroo.multieconomy.api.CurrencyAPI;
<<<<<<< HEAD
import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.voltzspawners.enums.PlayerAction;
import com.zpedroo.voltzspawners.listeners.PlayerChatListener;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.objects.Drop;
=======
import com.zpedroo.multieconomy.objects.Currency;
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.listeners.PlayerChatListener;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.objects.Manager;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import com.zpedroo.voltzspawners.objects.PlayerChat;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.FileUtils;
import com.zpedroo.voltzspawners.utils.builder.InventoryBuilder;
import com.zpedroo.voltzspawners.utils.builder.InventoryUtils;
import com.zpedroo.voltzspawners.utils.builder.ItemBuilder;
<<<<<<< HEAD
=======
import com.zpedroo.voltzspawners.utils.enums.Permission;
import com.zpedroo.voltzspawners.utils.enums.PlayerAction;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
<<<<<<< HEAD
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
=======
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import java.util.List;
import java.util.Map;
import java.util.UUID;

<<<<<<< HEAD
import static com.zpedroo.voltzspawners.utils.config.Messages.CHOOSE_AMOUNT;
=======
import static com.zpedroo.voltzspawners.utils.config.Messages.*;
import static com.zpedroo.voltzspawners.utils.config.Settings.TAX_REMOVE_STACK;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

<<<<<<< HEAD
    private final ItemStack nextPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).getFileConfiguration(), "Next-Page").build();
    private final ItemStack previousPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).getFileConfiguration(), "Previous-Page").build();

    public Menus() {
        instance = this;
=======
    private ItemStack nextPageItem;
    private ItemStack previousPageItem;

    public Menus() {
        instance = this;
        this.nextPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Next-Page").build();
        this.previousPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Previous-Page").build();
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        for (String items : FileUtils.get().getSection(file, "Inventory.items")) {
<<<<<<< HEAD
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), "Inventory.items." + items).build();
=======
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + items).build();
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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

<<<<<<< HEAD
=======
    public void openSpawnerMenu(Player player, PlacedSpawner spawner) {
        File folder = new File(VoltzSpawners.get().getDataFolder() + "/spawners/" + spawner.getSpawner().getType() + ".yml");
        FileConfiguration file = YamlConfiguration.loadConfiguration(folder);

        Manager manager = spawner.getManager(player.getUniqueId());

        String title = ChatColor.translateAlternateColorCodes('&', file.getString("Spawner-Menu.title"));
        int size = file.getInt("Spawner-Menu.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        for (String items : file.getConfigurationSection("Spawner-Menu.items").getKeys(false)) {
            ItemStack item = ItemBuilder.build(file, "Spawner-Menu.items." + items, new String[]{
                    "{owner}",
                    "{type}",
                    "{stack}",
                    "{drops_value}",
                    "{privacy_status}"
            }, new String[]{
                    Bukkit.getOfflinePlayer(spawner.getOwnerUUID()).getName(),
                    spawner.getSpawner().getTypeTranslated(),
                    NumberFormatter.getInstance().format(spawner.getStack()),
                    NumberFormatter.getInstance().format(spawner.getSpawner().getDropsValue()),
                    spawner.isPublic() ? PUBLIC : PRIVATE
            }).build();

            int slot = file.getInt("Spawner-Menu.items." + items + ".slot");
            String action = file.getString("Spawner-Menu.items." + items + ".action", "NULL");

            inventory.addItem(item, slot, () -> {
                switch (action.toUpperCase()) {
                    case "SWITCH_PRIVACY":
                        spawner.setPublic(!spawner.isPublic());
                        openSpawnerMenu(player, spawner);
                        player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
                        break;
                    case "MANAGERS":
                        openManagersMenu(player, spawner);
                        player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
                        break;
                    case "REMOVE_STACK":
                        if (!player.getUniqueId().equals(spawner.getOwnerUUID()) && (manager != null && !manager.hasPermission(Permission.REMOVE_STACK))) {
                            player.sendMessage(NEED_PERMISSION);
                            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 0.5f, 0.5f);
                            return;
                        }

                        PlayerChatListener.getPlayerChat().put(player, new PlayerChat(player, spawner, PlayerAction.REMOVE_STACK));
                        player.closeInventory();
                        clearChat(player);

                        for (String msg : REMOVE_STACK) {
                            player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                                    "{tax}"
                            }, new String[]{
                                    String.valueOf(TAX_REMOVE_STACK)
                            }));
                        }
                        break;
                }
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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

<<<<<<< HEAD
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), "Item", new String[]{
=======
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Item", new String[]{
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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

<<<<<<< HEAD
=======
    public void openManagersMenu(Player player, PlacedSpawner spawner) {
        FileUtils.Files file = FileUtils.Files.MANAGERS;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        int nextPageSlot = FileUtils.get().getInt(file, "Inventory.next-page-slot");
        int previousPageSlot = FileUtils.get().getInt(file, "Inventory.previous-page-slot");

        InventoryBuilder inventory = new InventoryBuilder(title, size, previousPageItem, previousPageSlot, nextPageItem, nextPageSlot);

        if (spawner.getManagers().isEmpty()) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Nothing").build();
            int slot = FileUtils.get().getInt(file, "Nothing.slot");

            inventory.addItem(item, slot);
        } else {
            String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");
            int i = -1;

            for (Manager manager : spawner.getManagers()) {
                if (++i >= slots.length) i = 0;

                ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Item", new String[]{
                        "{player}",
                        "{add_stack}",
                        "{remove_stack}",
                        "{add_friends}",
                        "{remove_friends}"
                }, new String[]{
                        Bukkit.getOfflinePlayer(manager.getUUID()).getName(),
                        manager.hasPermission(Permission.ADD_STACK) ? TRUE : FALSE,
                        manager.hasPermission(Permission.REMOVE_STACK) ? TRUE : FALSE,
                        manager.hasPermission(Permission.ADD_FRIENDS) ? TRUE : FALSE,
                        manager.hasPermission(Permission.REMOVE_FRIENDS) ? TRUE : FALSE
                }).build();
                int slot = Integer.parseInt(slots[i]);

                inventory.addItem(item, slot);

                if (spawner.getOwnerUUID().equals(player.getUniqueId()) || manager.hasPermission(Permission.REMOVE_FRIENDS)) {
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = meta.hasLore() ? (ArrayList<String>) meta.getLore() : new ArrayList<>();

                    for (String toAdd : FileUtils.get().getStringList(file, "Extra-Lore")) {
                        if (toAdd == null) break;

                        lore.add(ChatColor.translateAlternateColorCodes('&', toAdd));
                    }

                    meta.setLore(lore);
                    item.setItemMeta(meta);

                    inventory.addAction(slot, () -> {
                        openPermissionsMenu(player, spawner, manager);
                        player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
                    }, ActionType.ALL_CLICKS);
                }
            }
        }

        Manager manager = spawner.getManager(player.getUniqueId());

        ItemStack addFriendItem = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Add-Friend").build();
        int addFriendSlot = FileUtils.get().getInt(file, "Add-Friend.slot");

        inventory.addDefaultItem(addFriendItem, addFriendSlot, () -> {
            if (spawner.getOwnerUUID().equals(player.getUniqueId()) || (manager != null && manager.hasPermission(Permission.ADD_FRIENDS))) {
                PlayerChatListener.getPlayerChat().put(player, new PlayerChat(player, spawner, PlayerAction.ADD_FRIEND));
                player.closeInventory();
                clearChat(player);

                for (String msg : ADD_FRIEND) {
                    player.sendMessage(msg);
                }
                return;
            }

            player.sendMessage(NEED_PERMISSION);
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 0.5f, 0.5f);
        }, ActionType.ALL_CLICKS);

        ItemStack backItem = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Back").build();
        int backSlot = FileUtils.get().getInt(file, "Back.slot");

        inventory.addDefaultItem(backItem, backSlot, () -> {
            openSpawnerMenu(player, spawner);
            player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
        }, ActionType.ALL_CLICKS);

        inventory.open(player);
    }

    public void openPermissionsMenu(Player player, PlacedSpawner spawner, Manager manager) {
        FileUtils.Files file = FileUtils.Files.PERMISSIONS;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            Permission permission = null;

            try {
                permission = Permission.valueOf(str.toUpperCase());
            } catch (Exception ex) {
                // ignore
            }

            int slot = FileUtils.get().getInt(file, "Inventory.items." + str + ".slot");

            if (permission == null) {
                ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str, new String[]{
                        "{friend}"
                }, new String[]{
                        Bukkit.getOfflinePlayer(manager.getUUID()).getName()
                }).build();

                String action = FileUtils.get().getString(file, "Inventory.items." + str + ".action");

                inventory.addItem(item, slot, () -> {
                    switch (action.toUpperCase()) {
                        case "BACK":
                            openManagersMenu(player, spawner);
                            player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
                            break;
                        case "REMOVE":
                            spawner.getManagers().remove(manager);
                            spawner.setUpdate(true);
                            openManagersMenu(player, spawner);
                            player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
                            break;
                    }
                }, ActionType.ALL_CLICKS);
            } else {
                ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str + "." + (manager.hasPermission(permission) ? "true" : "false")).build();

                final Permission finalPermission = permission;

                inventory.addItem(item, slot, () -> {
                    if (!player.getUniqueId().equals(spawner.getOwnerUUID())) {
                        player.sendMessage(ONLY_OWNER);
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 0.5f, 0.5f);
                        return;
                    }

                    manager.setPermission(finalPermission, !manager.hasPermission(finalPermission));
                    spawner.setUpdate(true);
                    openPermissionsMenu(player, spawner, manager);
                    player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
                }, ActionType.ALL_CLICKS);
            }
        }

        inventory.open(player);
    }

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
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

<<<<<<< HEAD
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
=======
            BigInteger price = NumberFormatter.getInstance().filter(FileUtils.get().getString(file, "Inventory.items." + str + ".price", "0"))
                    .multiply(spawner.getDropsValue());
            String toGet = spawner.getPermission() != null && !player.hasPermission(spawner.getPermission()) ? "locked" : "unlocked";
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(),
                    FileUtils.get().getFile(file).get().contains("Inventory.items." + str + ".locked")
                            && FileUtils.get().getFile(file).get().contains("Inventory.items." + str + ".unlocked")
                            ? "Inventory.items." + str + "." + toGet
                            : "Inventory.items." + str, new String[]{
                            "{price}",
                            "{drops}",
                            "{mcmmo}",
                            "{type}"
                    }, new String[]{
                            StringUtils.replaceEach(currency.getAmountDisplay(), new String[]{
                                    "{amount}"
                            }, new String[]{
                                    NumberFormatter.getInstance().format(price)
                            }),
                            NumberFormatter.getInstance().format(spawner.getDropsValue()),
                            NumberFormatter.getInstance().formatDecimal(spawner.getMcMMOExp()),
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
                            spawner.getTypeTranslated()
                    }).build();
            int slot = Integer.parseInt(slots[i]);

            final Currency finalCurrency = currency;
            inventory.addItem(item, slot, () -> {
<<<<<<< HEAD
                if (price.signum() <= 0 || !spawner.isUnlocked(player)) return;
=======
                if (price.signum() <= 0) return;
                if (spawner.getPermission() != null && !player.hasPermission(spawner.getPermission())) return;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

                player.closeInventory();
                PlayerChatListener.getPlayerChat().put(player, new PlayerChat(player, spawner, finalCurrency, price, PlayerAction.BUY_SPAWNER));
                clearChat(player);

                for (String msg : CHOOSE_AMOUNT) {
                    player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                            "{item}",
                            "{price}",
<<<<<<< HEAD
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
=======
                            "{drops_value}",
                            "{type}"
                    }, new String[]{
                            spawner.getDisplayName(),
                            StringUtils.replaceEach(finalCurrency.getAmountDisplay(), new String[]{
                                    "{amount}"
                            }, new String[]{
                                    NumberFormatter.getInstance().format(price)
                            }),
                            NumberFormatter.getInstance().format(spawner.getDropsValue()),
                            spawner.getTypeTranslated()
                    }));
                }
            }, ActionType.ALL_CLICKS);
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
        }

        inventory.open(player);
    }

<<<<<<< HEAD
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

=======
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    private void clearChat(Player player) {
        for (int i = 0; i < 25; ++i) {
            player.sendMessage("");
        }
    }
}