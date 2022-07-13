package com.zpedroo.voltzspawners.commands;

import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.objects.PlayerData;
import com.zpedroo.voltzspawners.utils.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillAllCmd implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        PlayerData data = DataManager.getInstance().getPlayerData(player);

        boolean killAll = !data.isKillAll();
        data.setKillAll(killAll);

        if (killAll) {
            player.sendMessage(Messages.KILL_ALL_ENABLED);
        } else {
            player.sendMessage(Messages.KILL_ALL_DISABLED);
        }
        return false;
    }
}