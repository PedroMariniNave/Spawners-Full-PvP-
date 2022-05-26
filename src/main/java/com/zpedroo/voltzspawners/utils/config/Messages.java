package com.zpedroo.voltzspawners.utils.config;

import com.zpedroo.voltzspawners.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Messages {

<<<<<<< HEAD
    public static final String KILL_ALL_ENABLED = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.kill-all-enabled"));

    public static final String KILL_ALL_DISABLED = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.kill-all-disabled"));

    public static final String NEED_PERMISSION = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.need-permission"));

    public static final String INCORRECT_PICKAXE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.incorrect-pickaxe"));

=======
    public static final String NEED_PERMISSION = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.need-permission"));

    public static final String REMOVE_STACK_MIN = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.remove-stack-min"));

    public static final String REMOVE_STACK_SUCCESSFUL = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.remove-stack-successful"));

    public static final String INCORRECT_PICKAXE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.incorrect-pickaxe"));

    public static final String INCORRECT_GIFT_OWNER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.incorrect-gift-owner"));

    public static final String ONLY_OWNER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.only-owner"));

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    public static final String OFFLINE_PLAYER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.offline-player"));

    public static final String INVALID_SPAWNER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-spawner"));

    public static final String INVALID_AMOUNT = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-amount"));

    public static final String SPAWNER_USAGE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.spawner-usage"));

<<<<<<< HEAD
    public static final String LOCKED_SPAWNER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.locked-spawner"));
=======
    public static final String HAS_PERMISSION = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.has-permission"));

    public static final String SPAWNER_PERMISSION = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.spawner-permission"));
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

    public static final String INSUFFICIENT_CURRENCY = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.insufficient-currency"));

    public static final String NEAR_SPAWNER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.near-spawner"));

<<<<<<< HEAD
=======
    public static final String PUBLIC = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.public"));

    public static final String PRIVATE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.private"));

    public static final String TRUE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.true"));

    public static final String FALSE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.false"));

    public static final String WAIT = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.wait"));

    public static final String SECOND = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.second"));

    public static final String SECONDS = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.seconds"));

    public static final String MINUTE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.minute"));

    public static final String MINUTES = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.minutes"));

    public static final String HOUR = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.hour"));

    public static final String HOURS = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.hours"));

    public static final String DAY = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.day"));

    public static final String DAYS = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.days"));

    public static final String NOW = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.now"));

    public static final List<String> NEW_QUOTATION = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.new-quotation"));

    public static final List<String> ADD_FRIEND = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.add-friend"));

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    public static final List<String> CHOOSE_AMOUNT = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.choose-amount"));

    public static final List<String> SUCCESSFUL_PURCHASED = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.successful-purchased"));

<<<<<<< HEAD
=======
    public static final List<String> REMOVE_STACK = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.remove-stack"));

>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
    private static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private static List<String> getColored(List<String> list) {
        List<String> colored = new ArrayList<>(list.size());
        for (String str : list) {
            colored.add(getColored(str));
        }

        return colored;
    }
}
