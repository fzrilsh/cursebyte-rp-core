package com.cursebyte.plugin.ui.menus.wallet;

import java.sql.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cursebyte.plugin.economy.Transaction;
import com.cursebyte.plugin.economy.TransactionManager;
import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuRouter;
import com.cursebyte.plugin.utils.SkullUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class MutationMenu implements Menu {

    @Override
    public String id() {
        return "wallet.mutation";
    }

    @Override
    public Inventory build(Player p, MenuContext ctx) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("MUTASI REKENING"));

        int page = ctx.get("page", Integer.class);
        int limit = 28;
        int offset = page * limit;

        List<Transaction> txs = TransactionManager.getTransactions(p.getUniqueId(), limit, offset);

        int slot = 10;
        for (Transaction tx : txs) {
            inv.setItem(slot++, txItem(p, tx));
            if (slot == 17)
                slot = 19;
            if (slot == 26)
                slot = 28;
        }

        inv.setItem(45, navItem("Sebelumnya", page - 1, false));
        inv.setItem(49, backItem());
        inv.setItem(53, navItem("Selanjutnya", page + 1, true));

        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        int page = ctx.get("page", Integer.class);

        if(slot == 49){
            MenuRouter.open(p, "wallet");
        }

        if (slot == 45) {
            MenuContext newCtx = new MenuContext();
            newCtx.set("page", Math.max(0, page - 1));

            MenuRouter.open(p, "wallet.mutation", newCtx);
        }

        if (slot == 53) {
            MenuContext newCtx = new MenuContext();
            newCtx.set("page", Math.max(0, page + 1));

            MenuRouter.open(p, "wallet.mutation", newCtx);
        }
    }

    private static ItemStack txItem(Player viewer, Transaction tx) {
        boolean isSender = tx.getSender().equals(viewer.getUniqueId().toString());

        Material mat = isSender ? Material.REDSTONE : Material.EMERALD;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(isSender ? "📤 Transfer Keluar" : "📥 Transfer Masuk")
                .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text(""),
                Component.text("Jumlah: ⛁ " + tx.getAmount()),
                Component.text(""),
                Component.text("Waktu: " + new Date(tx.getTime()).toString())));

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack navItem(String name, int page, boolean direction) {
        ItemStack item;
        if (direction) {
            item = SkullUtils.getCustomHead(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjcxNmNhMzk1MTFhOTY3MjBjMzM3OWU3NzE5NjNiZWZlMjI0YjYwZWNjZWQ5ZTY5MzQ5NTk3NWVkYTgxZGU3MiJ9fX0=");
        } else {
            item = SkullUtils.getCustomHead(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOThiNWU5ZDVhZmFjMTgzZjFmNTcwYzFiNmVmNTE1NmMxMjFjMWVmYmQ4NTUyN2Q4ZDc5ZDBhZGVlYjY3MjQ4NSJ9fX0=");
        }
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(name).decorate(TextDecoration.BOLD));
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
