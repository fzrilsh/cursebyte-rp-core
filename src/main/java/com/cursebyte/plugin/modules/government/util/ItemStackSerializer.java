package com.cursebyte.plugin.modules.government.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class ItemStackSerializer {

    private ItemStackSerializer() {
    }

    public static String toBase64(ItemStack item) {
        if (item == null) {
            throw new IllegalArgumentException("ItemStack cannot be null");
        }

        YamlConfiguration yml = new YamlConfiguration();
        yml.set("i", item);

        String yamlString = yml.saveToString();
        return Base64.getEncoder()
                .encodeToString(yamlString.getBytes(StandardCharsets.UTF_8));
    }

    public static ItemStack fromBase64(String base64) {
        if (base64 == null || base64.isEmpty())
            return null;

        try {
            byte[] decoded = Base64.getDecoder().decode(base64);
            String yamlString = new String(decoded, StandardCharsets.UTF_8);

            YamlConfiguration yml = new YamlConfiguration();
            yml.loadFromString(yamlString);

            ItemStack item = yml.getItemStack("i");
            if (item == null) {
                throw new IllegalStateException("Decoded YAML but item was null");
            }
            return item;

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to deserialize ItemStack from base64", e);
        }
    }
}