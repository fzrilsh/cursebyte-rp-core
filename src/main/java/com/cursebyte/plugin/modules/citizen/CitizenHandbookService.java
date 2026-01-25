package com.cursebyte.plugin.modules.citizen;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CitizenHandbookService {

    public static void give(Player player) {
        ItemStack guideBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) guideBook.getItemMeta();

        meta.title(Component.text("📘 Panduan Warga"));
        meta.author(Component.text("Sistem Nasional"));

        meta.pages(List.of(
                Component.text("🇮🇩 SELAMAT DATANG\n\nGunakan /app untuk mengakses seluruh sistem."),
                Component.text("💳 Sistem ekonomi digital\nTransfer • Rekening • Mutasi"),
                Component.text("🧑‍💼 Sistem Job\nDaftar kerja lewat aplikasi"),
                Component.text("🆔 Identitas Digital\nSemua player punya identitas nasional")
        ));

        guideBook.setItemMeta(meta);
        player.getInventory().addItem(guideBook);
    }
}