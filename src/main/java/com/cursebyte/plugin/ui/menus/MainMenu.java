package com.cursebyte.plugin.ui.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuRouter;
import com.cursebyte.plugin.ui.core.MenuSession;
import com.cursebyte.plugin.utils.SkullUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MainMenu implements Menu {

    @Override
    public String id() {
        return "main";
    }

    @Override
    public Inventory build(Player p, MenuContext ctx) {
        Inventory inv = Bukkit.createInventory(null, 45, Component.text("APLIKASI NASIONAL"));

        ItemStack grayGlass = glass(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack blackGlass = glass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 45; i++) {
            inv.setItem(i, grayGlass);
        }
        for (int i = 0; i < 9; i++)
            inv.setItem(i, blackGlass);
        for (int i = 36; i < 45; i++)
            inv.setItem(i, blackGlass);

        inv.setItem(20, walletItem());
        inv.setItem(22, reportItem());
        inv.setItem(40, closeItem());

        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        if (slot == 20) {
            MenuRouter.open(p, "wallet");
        }

        if (slot == 22) {
            MenuRouter.open(p, "report");
        }

        if (slot == 40) {
            p.closeInventory();
            MenuSession.clear(p);
        }
    }

    private static ItemStack glass(Material color) {
        ItemStack item = new ItemStack(color);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(" "));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack reportItem() {
        ItemStack item = SkullUtils.getCustomHead(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlNTIyZDkxODI1MjE0OWU2ZWRlMmVkZjNmZTBmMmMyYzU4ZmVlNmFjMTFjYjg4YzYxNzIwNzIxOGFlNDU5NSJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("🚨 Pusat Laporan")
                        .color(TextColor.color(255, 85, 85))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Laporkan pelanggaran atau bug")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),

                Component.text(""),

                Component.text("• Laporkan Player").color(TextColor.color(120, 255, 120)),
                Component.text("• Panggil Admin").color(TextColor.color(120, 255, 120)),

                Component.text(""),

                Component.text("Klik untuk membuka")
                        .color(TextColor.color(255, 255, 255))
                        .decorate(TextDecoration.BOLD)));

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack walletItem() {
        ItemStack item = SkullUtils.getCustomHead(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("💳 Rekening")
                        .color(TextColor.color(0, 255, 180))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Kelola keuanganmu")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),

                Component.text(""),

                Component.text("• Saldo").color(TextColor.color(120, 255, 120)),
                Component.text("• Mutasi rekening").color(TextColor.color(120, 255, 120)),

                Component.text(""),
                Component.text("Klik untuk membuka")
                        .color(TextColor.color(255, 255, 255))
                        .decorate(TextDecoration.BOLD)));

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack closeItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("✖ Tutup")
                        .color(TextColor.color(255, 80, 80))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Keluar dari aplikasi")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC)));

        item.setItemMeta(meta);
        return item;
    }
}