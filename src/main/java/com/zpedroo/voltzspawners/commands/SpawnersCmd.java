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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;

        if (args.length > 0) {
            switch (args[0].toUpperCase()) {
                case "TOP":
                    if (player == null) return true;

                    Menus.getInstance().openTopSpawnersMenu(player);
                    return true;
<<<<<<< HEAD
                case "SHOP":
                    if (player == null) return true;

                    Menus.getInstance().openShopMenu(player);
                    return true;
=======
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
                case "GIVE":
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

<<<<<<< HEAD
                    if (amount.compareTo(BigInteger.valueOf(2304)) > 0) amount = BigInteger.valueOf(2304);

                    target.getInventory().addItem(spawner.getItem(amount.intValue()));
=======
                    target.getInventory().addItem(spawner.getItem(amount));
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
                    return true;
            }
        }

        if (player == null) return true;

        Menus.getInstance().openMainMenu(player);
        player.playSound(player.getLocation(), Sound.HORSE_SADDLE, 0.5f, 10f);
        return false;
    }
}