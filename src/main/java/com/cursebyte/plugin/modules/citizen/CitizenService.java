package com.cursebyte.plugin.modules.citizen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.kyori.adventure.text.Component;

public class CitizenService {

    public static void init() {
        CitizenRepository.initTable();
    }

    public static void createCitizen(UUID uuid) {
        CitizenRepository.createIfNotExists(uuid);
    }

    public static boolean isLegal(UUID uuid) {
        return CitizenRepository.isRegistered(uuid);
    }

    public static CitizenProfile getProfile(UUID uuid){
        return CitizenRepository.get(uuid);
    }

    public static void register(UUID uuid, String realName) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String randomCode = String.valueOf((int) (Math.random() * 9000) + 1000);
        String nik = date + "-" + randomCode;

        CitizenRepository.update(
                uuid,
                nik,
                realName,
                System.currentTimeMillis(),
                true
        );
    }

    public static void giveHandbook(Player player) {
        ItemStack guideBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) guideBook.getItemMeta();

        meta.title(Component.text("📘 Panduan Warga"));
        meta.author(Component.text("Sistem Nasional"));

        List<Component> pages = new ArrayList<>();

        pages.add(Component.text("""
                🇮🇩 SELAMAT DATANG

                Selamat datang di Sistem Nasional!

                Server ini menggunakan sistem digital modern berbasis aplikasi.

                Semua fitur bisa diakses melalui:

                /app

                Gunakan menu ini untuk mengakses:
                • Rekening
                • Profil
                • Job
                • Transfer
                • Identitas
                • Layanan publik
                """));

        pages.add(Component.text("""
                📱 APLIKASI NASIONAL

                Command utama:
                /app

                Di dalam aplikasi:
                • Dompet / Rekening
                • Mutasi transfer
                • Transfer saldo
                • Profil warga
                • Identitas digital
                • Sistem pekerjaan
                • Layanan negara
                """));

        pages.add(Component.text("""
                💳 SISTEM EKONOMI

                Mata uang: ⛁ Dollar Digital

                Fitur:
                • Transfer antar player
                • Riwayat transaksi
                • Dompet digital
                • Sistem rekening
                • Saldo real-time

                Semua via menu aplikasi
                """));

        pages.add(Component.text("""
                🧑‍💼 SISTEM JOB

                Cara dapat pekerjaan:
                1. Buka /app
                2. Masuk menu Job
                3. Pilih pekerjaan
                4. Daftar
                5. Mulai bekerja

                Setiap job punya:
                • Gaji
                • Level
                • Rank
                • Skill
                • Bonus
                """));

        pages.add(Component.text("""
                🆔 IDENTITAS DIGITAL

                Setiap player punya:
                • UUID Nasional
                • Identitas warga
                • Nomor induk
                • Profil digital

                Data ini digunakan untuk:
                • Ekonomi
                • Job
                • Bank
                • Transfer
                • Layanan publik
                """));

        pages.add(Component.text("""
                📌 TIPS

                • Semua sistem lewat UI
                • Tidak perlu command manual
                • Gunakan menu
                • Sistem otomatis
                • Cross-platform (Java & Bedrock)
                • Mobile friendly

                Server ini berbasis sistem digital
                """));

        meta.pages(pages);
        guideBook.setItemMeta(meta);

        player.getInventory().addItem(guideBook);
    }
}