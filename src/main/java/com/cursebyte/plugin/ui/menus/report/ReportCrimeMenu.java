package com.cursebyte.plugin.ui.menus.report;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cursebyte.plugin.modules.report.CrimeType;
import com.cursebyte.plugin.modules.report.ReportSessionManager;
import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ReportCrimeMenu implements Menu {

    public String id() {
        return "report.crime";
    }

    public Inventory build(Player p, MenuContext ctx) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Jenis Kejahatan"));

        int slot = 0;
        for (CrimeType type : CrimeType.values()) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(type.name()));
            item.setItemMeta(meta);
            inv.setItem(slot++, item);
        }

        return inv;
    }

    public void onClick(Player p, int slot, MenuContext ctx) {
        ItemStack item = p.getOpenInventory().getItem(slot);
        if (item == null)
            return;

        String crime = PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName());
        ReportSessionManager.setCrime(p.getUniqueId(), crime);

        p.sendMessage("");
        p.sendMessage("§e§lFORMULIR PELAPORAN");
        p.sendMessage("§e⚠ Instruksi:");
        p.sendMessage("§fSilakan ketik §nkronologi kejadian§f atau detail laporan di chat.");
        p.sendMessage("");

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
        p.closeInventory();
    }
}