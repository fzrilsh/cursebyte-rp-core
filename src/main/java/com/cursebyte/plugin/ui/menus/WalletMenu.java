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

import com.cursebyte.plugin.modules.economy.EconomyService;
import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuRouter;
import com.cursebyte.plugin.utils.SkullUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class WalletMenu implements Menu {

    @Override
    public String id() {
        return "wallet";
    }

    @Override
    public Inventory build(Player p, MenuContext ctx) {
        Inventory inv = Bukkit.createInventory(null, 45, Component.text("REKENING"));

        ItemStack gray = glass(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack black = glass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 45; i++)
            inv.setItem(i, gray);
        for (int i = 0; i < 9; i++)
            inv.setItem(i, black);
        for (int i = 36; i < 45; i++)
            inv.setItem(i, black);

        double balance = EconomyService.getBalance(p.getUniqueId());

        inv.setItem(21, saldoItem(balance));
        inv.setItem(23, mutasiItem());
        inv.setItem(40, backItem());

        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        if(slot == 40){
            MenuRouter.open(p, "main");
        }

        if(slot == 23){
            MenuContext nctx = new MenuContext();
            nctx.set("page", 0);
            MenuRouter.open(p, "wallet.mutation", nctx);
        }
    }

    private static ItemStack saldoItem(double balance) {
        ItemStack item = SkullUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjZlYTk5YzhjNjIxMzZmNDlkOWMyOTQ3YzQ0Y2Y0YjFmYzkzNzI0OWRjMWVlMjAxZjUxMTA5NjA4NGIyMTQwNiJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("💰 Saldo Rekening")
                        .color(TextColor.color(0, 255, 180))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Saldo tersedia")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),

                Component.text(""),
                Component.text("⛁ " + format(balance))
                        .color(TextColor.color(120, 255, 120))
                        .decorate(TextDecoration.BOLD)));

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack mutasiItem() {
        ItemStack item = SkullUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDgwNjQ0MGY1NTg4NjQ5NDdkYzA5MzI2NTAwNmVhODBkNzE0NTI0NDQyYjhhMDA5MDZmMmZiMDc1MDc3Y2ViMyJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("📒 Mutasi Rekening")
                        .color(TextColor.color(120, 180, 255))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Riwayat transaksi")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),

                Component.text(""),
                Component.text("Klik untuk membuka")
                        .color(TextColor.color(255, 255, 255))
                        .decorate(TextDecoration.BOLD)));

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

    private static ItemStack glass(Material material) {
        ItemStack glass = new ItemStack(material);
        ItemMeta meta = glass.getItemMeta();

        meta.displayName(Component.text(" "));
        glass.setItemMeta(meta);

        return glass;
    }

    private static String format(double val) {
        return String.format("%.2f", val);
    }
}