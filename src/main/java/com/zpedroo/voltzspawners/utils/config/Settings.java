package com.zpedroo.voltzspawners.utils.config;

import com.zpedroo.voltzspawners.utils.FileUtils;
import com.zpedroo.voltzspawners.utils.color.Colorize;

public class Settings {

    public static final String LOOTING_BONUS = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.looting-bonus");

    public static final int STACK_RADIUS = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.stack-radius");

    public static final int SPAWNER_UPDATE = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.spawner-update");

    public static final long SAVE_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.save-interval");

    public static final String[] SPAWNER_HOLOGRAM = Colorize.getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.hologram")).toArray(new String[0]);
}
