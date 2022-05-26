package com.zpedroo.voltzspawners.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class FileUtils {

    private static FileUtils instance;
    public static FileUtils get() { return instance; }

    private final Plugin plugin;
    private final Map<Files, FileManager> files;

    public FileUtils(Plugin plugin) {
        instance = this;
        this.plugin = plugin;
        this.files = new HashMap<>(Files.values().length);

        for (Files files : Files.values()) {
            this.files.put(files, new FileManager(files));
        }
    }

    public String getString(Files file, String path) {
        return getString(file, path, "NULL");
    }

    public String getString(Files file, String path, String defaultValue) {
        return getFile(file).getFileConfiguration().getString(path, defaultValue);
    }

    public List<String> getStringList(Files file, String path) {
        return getFile(file).getFileConfiguration().getStringList(path);
    }

    public boolean getBoolean(Files file, String path) {
        return getFile(file).getFileConfiguration().getBoolean(path);
    }

    public int getInt(Files file, String path) {
        return getInt(file, path, 0);
    }

    public int getInt(Files file, String path, int defaultValue) {
        return getFile(file).getFileConfiguration().getInt(path, defaultValue);
    }

    public long getLong(Files file, String path) {
        return getLong(file, path, 0);
    }

    public long getLong(Files file, String path, long defaultValue) {
        return getFile(file).getFileConfiguration().getLong(path, defaultValue);
    }

    public double getDouble(Files file, String path) {
        return getDouble(file, path, 0);
    }

    public double getDouble(Files file, String path, double defaultValue) {
        return getFile(file).getFileConfiguration().getDouble(path, defaultValue);
    }

    public float getFloat(Files file, String path) {
        return getFloat(file, path, 0);
    }

    public float getFloat(Files file, String path, float defaultValue) {
        return (float) getFile(file).getFileConfiguration().getDouble(path, defaultValue);
    }

    public Set<String> getSection(Files file, String path) {
        return getFile(file).getFileConfiguration().getConfigurationSection(path).getKeys(false);
    }

    public FileManager getFile(Files file) {
        return this.files.get(file);
    }

    private void copy(InputStream is, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;

            while ((len=is.read(buf)) > 0) {
                out.write(buf,0,len);
            }

            out.close();
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public enum Files {
        CONFIG("config", "configuration-files", "", false),
        TOP_SPAWNERS("top_spawners", "menus", "menus", false),
        SHOP("shop", "menus", "menus", false),
        MAIN("main", "menus", "menus", false),
        DROPS_PREVIEW("drops_preview", "menus", "menus", false),
        CHICKEN("chicken", "spawners", "spawners", true);

        private final String name;
        private final String resource;
        private final String folder;
        private final boolean requireEmpty;

        Files(String name, String resource, String folder, boolean requireEmpty) {
            this.name = name;
            this.resource = resource;
            this.folder = folder;
            this.requireEmpty = requireEmpty;
        }

        public String getName() {
            return name;
        }

        public String getResource() {
            return resource;
        }

        public String getFolder() {
            return folder;
        }

        public boolean hasRequiredEmpty() {
            return requireEmpty;
        }
    }

    public class FileManager {

        private final File file;
        private FileConfiguration fileConfig;

        public FileManager(Files file) {
            this.file = new File(plugin.getDataFolder() + (file.getFolder().isEmpty() ? "" : "/" + file.getFolder()), file.getName() + ".yml");

            if (!this.file.exists()) {
                if (file.hasRequiredEmpty()) {
                    File folder = new File(plugin.getDataFolder(), file.getFolder());
                    if (folder.listFiles() != null) {
                        if (Stream.of(folder.listFiles()).map(YamlConfiguration::loadConfiguration).count() > 0) return;
                    }
                }

                try {
                    this.file.getParentFile().mkdirs();
                    this.file.createNewFile();

                    copy(plugin.getResource((file.getResource().isEmpty() ? "" : file.getResource() + "/") + file.getName() + ".yml"), this.file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), StandardCharsets.UTF_8));
                fileConfig = YamlConfiguration.loadConfiguration(reader);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public FileConfiguration getFileConfiguration() {
            return fileConfig;
        }

        public File getFile() {
            return file;
        }

        public void save() {
            try {
                fileConfig.save(file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}