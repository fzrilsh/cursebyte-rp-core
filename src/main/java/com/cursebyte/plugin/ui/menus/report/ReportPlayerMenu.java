package com.cursebyte.plugin.ui.menus.report;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cursebyte.plugin.modules.report.ReportSessionManager;
import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuRouter;
import com.cursebyte.plugin.utils.SkullUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ReportPlayerMenu implements Menu {

    public String id() {
        return "report.players";
    }

    public Inventory build(Player p, MenuContext ctx) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Pilih Pemain"));

        int slot = 0;
        for (Player target : Bukkit.getOnlinePlayers()) {
            ItemStack head = SkullUtils.getPlayerHead(target.getUniqueId());
            ItemMeta meta = head.getItemMeta();

            meta.displayName(Component.text(target.getName()));
            head.setItemMeta(meta);

            inv.setItem(slot++, head);
        }
        
        return inv;
    }

    public void onClick(Player p, int slot, MenuContext ctx) {
        ItemStack item = p.getOpenInventory().getItem(slot);
        if (item == null)
            return;

        String targetName = PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName());
        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null)
            return;

        ReportSessionManager.setTarget(p.getUniqueId(), target.getUniqueId());
        MenuRouter.open(p, "report.crime", ctx);
    }
}