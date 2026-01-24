package com.cursebyte.plugin.ui.menus;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cursebyte.plugin.player.TransferManager;
import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuSession;
import com.cursebyte.plugin.utils.SkullUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class TransferMenu implements Menu {

    @Override
    public String id() {
        return "transfer";
    }

    @Override
    public Inventory build(Player p, MenuContext ctx) {
        UUID targetId = ctx.get("target", UUID.class);
        Player target = Bukkit.getPlayer(targetId);

        Inventory inv = Bukkit.createInventory(null, 45, Component.text("INTERAKSI DENGAN " + target.getName()));

        ItemStack gray = glass(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack black = glass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 45; i++)
            inv.setItem(i, gray);
        for (int i = 0; i < 9; i++)
            inv.setItem(i, black);
        for (int i = 36; i < 45; i++)
            inv.setItem(i, black);

        inv.setItem(40, closeItem());
        inv.setItem(22, transferItem(target));

        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        if (slot == 40) {
            p.closeInventory();
            MenuSession.clear(p);
        }

        if (slot == 22) {
            UUID targetId = ctx.get("target", UUID.class);
            Player target = Bukkit.getPlayer(targetId);

            if (!target.isOnline()) {
                p.sendMessage("§cPlayer tujuan sudah offline.");
                return;
            }

            p.closeInventory();
            TransferManager.startTransferSession(p, target);

            p.sendMessage("§e========================================");
            p.sendMessage("§eSedang mentransfer ke: §f" + target.getName());
            p.sendMessage("§aSilakan ketik jumlah uang di chat.");
            p.sendMessage("§7(Ketik 'batal' untuk membatalkan)");
            p.sendMessage("§e========================================");
        }
    }

    public static void open(Player source, Player target) {
        Inventory inv = Bukkit.createInventory(null, 45, Component.text("INTERAKSI DENGAN " + target.getName()));

        ItemStack gray = glass(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack black = glass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 45; i++)
            inv.setItem(i, gray);
        for (int i = 0; i < 9; i++)
            inv.setItem(i, black);
        for (int i = 36; i < 45; i++)
            inv.setItem(i, black);

        inv.setItem(40, closeItem());
        inv.setItem(22, transferItem(target));

        source.openInventory(inv);
    }

    private static ItemStack transferItem(Player target) {
        ItemStack item = SkullUtils.getCustomHead(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWJkYTVmMzE5MzdiMmZmNzU1MjcxZDk3ZjAxYmU4NGQ1MmE0MDdiMzZjYTc3NDUxODU2MTYyYWM2Y2ZiYjM0ZiJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("🔁 Transfer Uang")
                        .color(TextColor.color(120, 180, 255))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Kirim uang ke " + target.getName())
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),

                Component.text(""),
                Component.text("Klik untuk Mengirim uang")
                        .color(TextColor.color(255, 255, 255))
                        .decorate(TextDecoration.BOLD)));

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

    private static ItemStack glass(Material material) {
        ItemStack glass = new ItemStack(material);
        ItemMeta meta = glass.getItemMeta();

        meta.displayName(Component.text(" "));
        glass.setItemMeta(meta);

        return glass;
    }

}
