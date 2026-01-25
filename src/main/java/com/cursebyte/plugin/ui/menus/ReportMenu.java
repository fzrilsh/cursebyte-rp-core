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

import com.cursebyte.plugin.modules.report.ReportSessionManager;
import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuRouter;
import com.cursebyte.plugin.utils.SkullUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ReportMenu implements Menu {

    @Override
    public String id() {
        return "report";
    }

    @Override
    public Inventory build(Player p, MenuContext ctx) {
        Inventory inv = Bukkit.createInventory(null, 45, Component.text("PUSAT LAPORAN"));

        ItemStack grayGlass = glass(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack blackGlass = glass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 45; i++) {
            inv.setItem(i, grayGlass);
        }
        for (int i = 0; i < 9; i++)
            inv.setItem(i, blackGlass);
        for (int i = 36; i < 45; i++)
            inv.setItem(i, blackGlass);

        inv.setItem(21, reportPlayerItem());
        inv.setItem(23, callAdminItem());
        inv.setItem(40, backItem());

        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        if (slot == 21) {
            ReportSessionManager.start(p.getUniqueId());
            MenuRouter.open(p, "report.players");
        }

        if (slot == 23) {
            MenuRouter.open(p, "report");
        }

        if (slot == 40) {
            MenuRouter.open(p, "main");
        }
    }

    private static ItemStack glass(Material color) {
        ItemStack item = new ItemStack(color);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(" "));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack reportPlayerItem() {
        ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("👮 Laporkan Pelanggar")
                        .color(TextColor.color(255, 85, 85))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Ada player yang melanggar aturan?")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),

                Component.text(""),

                Component.text("Klik untuk lapor")
                        .color(TextColor.color(255, 255, 255))
                        .decorate(TextDecoration.BOLD)));

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack callAdminItem() {
        ItemStack item = SkullUtils.getCustomHead(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWM2NWVhNjFiMDJiM2E4YTAyNTlhNmJlNjgwYjM5NmQ2YmIzYWMwYTk3Mzk2Y2QzZjg5ZWNjM2NlODM5MDMyMSJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("📞 Panggil Admin")
                        .color(TextColor.color(255, 170, 0))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Butuh bantuan staff?")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),

                Component.text(""),

                Component.text("• Bug / Error").color(TextColor.color(255, 255, 85)),
                Component.text("• Player Tersangkut").color(TextColor.color(255, 255, 85)),
                Component.text("• Pertanyaan Umum").color(TextColor.color(255, 255, 85)),

                Component.text(""),
                Component.text("Klik untuk memanggil")
                        .color(TextColor.color(255, 255, 255))
                        .decorate(TextDecoration.BOLD)));

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack backItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("← Kembali")
                        .color(TextColor.color(255, 200, 120))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Kembali ke menu utama")
                        .color(TextColor.color(180, 180, 180))));

        item.setItemMeta(meta);
        return item;
    }
}