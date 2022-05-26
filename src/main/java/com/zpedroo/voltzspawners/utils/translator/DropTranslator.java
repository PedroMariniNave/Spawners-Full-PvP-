package com.zpedroo.voltzspawners.utils.translator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DropTranslator {

    private static DropTranslator instance;
    public static DropTranslator getInstance() { return instance; }

    private Map<Material, String> translations;

    public DropTranslator(FileConfiguration file) {
        instance = this;
        this.translations = new HashMap<>(16);

        Material material = null;
        String translation = null;
        String[] translationSplit = null;
        for (String str : file.getStringList("Drops-Translator")) {
            if (str == null) continue;

            translationSplit = str.split(",");
            material = Material.getMaterial(translationSplit[0].toUpperCase());
            if (material == null) continue;

            translation = ChatColor.translateAlternateColorCodes('&', translationSplit[1]);

            translations.put(material, translation);
        }
    }

    public String translate(ItemStack item) {
        if (!translations.containsKey(item.getType())) return item.getType().toString();

        return translations.get(item.getType());
    }
}