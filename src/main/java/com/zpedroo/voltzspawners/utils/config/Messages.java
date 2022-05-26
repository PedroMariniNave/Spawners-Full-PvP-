package com.zpedroo.voltzspawners.utils.config;

import com.zpedroo.voltzspawners.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static final String KILL_ALL_ENABLED = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.kill-all-enabled"));

    public static final String KILL_ALL_DISABLED = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.kill-all-disabled"));

    public static final String NEED_PERMISSION = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.need-permission"));

    public static final String INCORRECT_PICKAXE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.incorrect-pickaxe"));

    public static final String OFFLINE_PLAYER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.offline-player"));

    public static final String INVALID_SPAWNER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-spawner"));

    public static final String INVALID_AMOUNT = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-amount"));

    public static final String SPAWNER_USAGE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.spawner-usage"));

    public static final String LOCKED_SPAWNER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.locked-spawner"));

    public static final String INSUFFICIENT_CURRENCY = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.insufficient-currency"));

    public static final String NEAR_SPAWNER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.near-spawner"));

    public static final List<String> CHOOSE_AMOUNT = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.choose-amount"));

    public static final List<String> SUCCESSFUL_PURCHASED = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.successful-purchased"));

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
