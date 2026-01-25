package com.cursebyte.plugin.listener;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

import com.cursebyte.plugin.CursebyteCore;
import com.cursebyte.plugin.modules.report.ReportService;
import com.cursebyte.plugin.modules.report.ReportSessionManager;
import com.cursebyte.plugin.modules.report.ReportSessionManager.ReportSession;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ReportListener implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent e) {
        Player p = e.getPlayer();

        if (!ReportSessionManager.hasSession(p.getUniqueId()))
            return;

        e.setCancelled(true);
        ReportSession session = ReportSessionManager.get(p.getUniqueId());

        if (session.step == ReportSessionManager.Step.WRITE_DESCRIPTION){
            Player target = Bukkit.getPlayer(session.target);
            String description = PlainTextComponentSerializer.plainText().serialize(e.message());
            ReportSessionManager.setDescription(p.getUniqueId(), description);
    
            p.sendMessage("");
            p.sendMessage("§eKonfirmasi Laporan");
            p.sendMessage("§7Target : §f" + target.getName());
            p.sendMessage("§7Jenis  : §f" + session.crimeType);
            p.sendMessage("§7Detail : §f" + session.description);
            p.sendMessage("");
            p.sendMessage("§aKetik: YA  → Kirim laporan");
            p.sendMessage("§cKetik: TIDAK → Batalkan");
            p.sendMessage("");

            session.step = ReportSessionManager.Step.CONFIRMATION;
        } else if (session.step == ReportSessionManager.Step.CONFIRMATION){
            String msg = PlainTextComponentSerializer.plainText().serialize(e.message()).toLowerCase();
    
            Bukkit.getScheduler().runTask(CursebyteCore.getInstance(), () -> {
                if (msg.equals("ya") || msg.equals("y")) {
                    ReportService.submit(
                            session.reporter,
                            session.target,
                            session.crimeType,
                            session.description);
    
                    p.sendMessage("§aLaporan berhasil dikirim.");
                    p.sendMessage("§7Terima kasih telah membantu menjaga ketertiban kota.");
    
                    p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
    
                    ReportSessionManager.end(p.getUniqueId());
                } else if (msg.equals("tidak") || msg.equals("no") || msg.equals("n")) {
                    p.sendMessage("§cLaporan dibatalkan.");
                    ReportSessionManager.end(p.getUniqueId());
                } else {
                    p.sendMessage("§eKetik:");
                    p.sendMessage("§aYA §7→ Kirim laporan");
                    p.sendMessage("§cTIDAK §7→ Batalkan laporan");
                }
            });
        }
    }
}
