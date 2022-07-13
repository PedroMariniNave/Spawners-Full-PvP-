package com.zpedroo.voltzspawners.utils.config;

import com.zpedroo.voltzspawners.utils.FileUtils;
import com.zpedroo.voltzspawners.utils.color.Colorize;

import java.util.List;

public class Messages {

    public static final String KILL_ALL_ENABLED = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.kill-all-enabled"));

    public static final String KILL_ALL_DISABLED = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.kill-all-disabled"));

    public static final String NEED_PERMISSION = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.need-permission"));

    public static final String INCORRECT_PICKAXE = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.incorrect-pickaxe"));

    public static final String OFFLINE_PLAYER = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.offline-player"));

    public static final String INVALID_SPAWNER = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-spawner"));

    public static final String INVALID_AMOUNT = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-amount"));

    public static final String SPAWNER_USAGE = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.spawner-usage"));

    public static final String LOCKED_SPAWNER = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.locked-spawner"));

    public static final String INSUFFICIENT_CURRENCY = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.insufficient-currency"));

    public static final String NEAR_SPAWNER = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.near-spawner"));

    public static final List<String> CHOOSE_AMOUNT = Colorize.getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.choose-amount"));

    public static final List<String> SUCCESSFUL_PURCHASED = Colorize.getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.successful-purchased"));
}
