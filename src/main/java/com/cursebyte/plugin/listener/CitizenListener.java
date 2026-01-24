package com.cursebyte.plugin.listener;

import java.time.Duration;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.cursebyte.plugin.CursebyteCore;
import com.cursebyte.plugin.modules.citizen.CitizenService;
import com.cursebyte.plugin.player.ImmigrationStateManager;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;

public class CitizenListener implements Listener {

    @EventHandler
    public void onNPCClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        int clickedId = event.getNPC().getId();

        FileConfiguration config = CursebyteCore.getInstance().getConfig();
        int immigrationNpcId = config.getInt("immigration.npc.id", -1);

        if (clickedId == immigrationNpcId && immigrationNpcId != -1) {
            if (ImmigrationStateManager.isInSession(player)) {
                player.sendMessage("§cSelesaikan input sebelumnya dulu!");
                return;
            }

            ImmigrationStateManager.startSession(player, "REGISTRATION_NAME");

            player.sendMessage("§e[Imigrasi] §fHalo! Selamat datang.");
            player.sendMessage("§e[Imigrasi] §fSilakan ketik §nNama Lengkap Anda§f di chat untuk pembuatan Data Diri.");

            player.playSound(event.getNPC().getStoredLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, 1f);
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        Player player = e.getPlayer();

        if (!CitizenService.isLegal(player.getUniqueId()) && !player.hasPermission("immigration.admin")
                && !ImmigrationStateManager.isInSession(player)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cAnda harus mengurus kewarganegaraan terlebih dahulu!");
        }

        String sessionType = ImmigrationStateManager.getSession(player);
        if ("REGISTRATION_NAME".equals(sessionType)) {
            e.setCancelled(true);

            String realName = PlainTextComponentSerializer.plainText().serialize(e.message());
            Bukkit.getScheduler().runTask(CursebyteCore.getInstance(), () -> {
                CitizenService.register(player.getUniqueId(), realName);

                Location spawnLoc = player.getWorld().getSpawnLocation();
                player.teleport(spawnLoc);
                player.setGameMode(GameMode.SURVIVAL);

                CitizenService.giveHandbook(player);

                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                player.showTitle(Title.title(
                        Component.text("RESMI LEGAL!").color(TextColor.color(0, 255, 180)),
                        Component.text("Selamat menikmati kota!"),
                        net.kyori.adventure.title.Title.Times.times(
                                Duration.ofMillis(500),
                                Duration.ofMillis(3000),
                                Duration.ofMillis(500))));

                ImmigrationStateManager.endSession(player);
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
                CursebyteCore.getInstance().getLogger().warning("Lokasi spawn imigrasi belum diatur! Gunakan /setupimmigration spawn");
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (!CitizenService.isLegal(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cSelesaikan administrasi imigrasi dulu!");
        }
    }
}