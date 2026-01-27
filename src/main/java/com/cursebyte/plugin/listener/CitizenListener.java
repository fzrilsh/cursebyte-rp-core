package com.cursebyte.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.cursebyte.plugin.CursebyteCore;
import com.cursebyte.plugin.modules.citizen.CitizenFlow;
import com.cursebyte.plugin.modules.citizen.CitizenProfile;
import com.cursebyte.plugin.modules.citizen.CitizenService;
import com.cursebyte.plugin.modules.citizen.CitizenSessionManager;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class CitizenListener implements Listener {

    @EventHandler
    public void onNPCClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        int clickedId = event.getNPC().getId();

        if (CitizenService.isLegal(player.getUniqueId())) {
            CitizenProfile profile = CitizenService.getProfile(player.getUniqueId());
            player.sendMessage("");
            player.sendMessage("§e[Imigrasi] §fKamu sudah terdaftar menjadi warga negara!");
            player.sendMessage("§eNama Panjang: " + profile.getFullName());
            player.sendMessage("§eNomor Induk Kependudukan: " + profile.getNik());
            player.sendMessage("§eTerdaftar Pada: " + profile.getJoinDate());
            player.sendMessage("");
            return;
        }

        FileConfiguration config = CursebyteCore.getInstance().getConfig();
        int immigrationNpcId = config.getInt("immigration.npc.id", -1);

        if (clickedId == immigrationNpcId && immigrationNpcId != -1) {
            if (CitizenSessionManager.isActive(player)) {
                player.sendMessage("§cSelesaikan input sebelumnya dulu!");
                return;
            }

            CitizenSessionManager.start(player, "REGISTRATION_NAME");

            player.sendMessage("§e[Imigrasi] §fHalo! Selamat datang.");
            player.sendMessage("§e[Imigrasi] §fSilakan ketik §nNama Lengkap Anda§f di chat untuk pembuatan Data Diri.");

            player.playSound(event.getNPC().getStoredLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, 1f);
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        Player player = e.getPlayer();

        if (!CitizenService.isLegal(player.getUniqueId())
                && !player.hasPermission("immigration.admin")
                && !CitizenSessionManager.isActive(player)) {

            e.setCancelled(true);
            player.sendMessage("§cAnda harus mengurus kewarganegaraan terlebih dahulu!");
            return;
        }

        String sessionType = CitizenSessionManager.get(player);

        if ("REGISTRATION_NAME".equals(sessionType)) {
            e.setCancelled(true);

            String realName = PlainTextComponentSerializer.plainText().serialize(e.message());

            Bukkit.getScheduler().runTask(CursebyteCore.getInstance(), () -> {
                Location spawn = player.getWorld().getSpawnLocation();
                CitizenFlow.completeRegistration(player, realName, spawn);
            });
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        CitizenService.createCitizen(p.getUniqueId());
        if (!CitizenService.isLegal(p.getUniqueId())) {
            Location imgSpawn = CursebyteCore.getInstance().getConfig().getLocation("immigration.spawn-location");

            if (imgSpawn != null) {
                p.teleport(imgSpawn);
                p.sendMessage("§eAnda dipindahkan ke kantor imigrasi.");
            } else {
                CursebyteCore.getInstance().getLogger()
                        .warning("Lokasi spawn imigrasi belum diatur! Gunakan /setupimmigration spawn");
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (!CitizenService.isLegal(e.getPlayer().getUniqueId()) && !e.getPlayer().hasPermission("immigration.admin")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cSelesaikan administrasi imigrasi dulu!");
        }
    }
}