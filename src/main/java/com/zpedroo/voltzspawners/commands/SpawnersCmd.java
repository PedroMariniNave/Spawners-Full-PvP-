package com.zpedroo.voltzspawners.commands;

import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.objects.Spawner;
import com.zpedroo.voltzspawners.utils.config.Messages;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import com.zpedroo.voltzspawners.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigInteger;

public class SpawnersCmd implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player player = sender instanceof Player ? (Player) sender : null;
        if (args.length > 0) {
            switch (args[0].toUpperCase()) {
                case "TOP": {
                    if (player != null) Menus.getInstance().openTopSpawnersMenu(player);
                    return true;
                }
                case "SHOP": {
                    if (player != null) Menus.getInstance().openShopMenu(player);
                    return true;
                }
                case "GIVE": {
                    if (!sender.hasPermission("spawners.admin")) break;
                    if (args.length < 4) {
                        sender.sendMessage(Messages.SPAWNER_USAGE);
                        return true;
                    }

                    Spawner spawner = DataManager.getInstance().getSpawner(args[2]);
                    if (spawner == null) {
                        sender.sendMessage(Messages.INVALID_SPAWNER);
                        return true;
                    }

                    BigInteger amount = NumberFormatter.getInstance().filter(args[3]);
                    if (amount.signum() <= 0) {
                        sender.sendMessage(Messages.INVALID_AMOUNT);
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(Messages.OFFLINE_PLAYER);
                        return true;
                    }

                    if (amount.compareTo(BigInteger.valueOf(2304L)) > 0) amount = BigInteger.valueOf(2304L);

                    target.getInventory().addItem(spawner.getItem(amount.intValue()));
                    return true;
                }
            }
        }

        if (player == null) return true;

        Menus.getInstance().openMainMenu(player);
        player.playSound(player.getLocation(), Sound.HORSE_SADDLE, 0.5f, 10.0f);
        return false;
    }
}
