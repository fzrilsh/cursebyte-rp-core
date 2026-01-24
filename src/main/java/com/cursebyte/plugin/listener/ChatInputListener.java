package com.cursebyte.plugin.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.cursebyte.plugin.economy.TransactionSession;
import com.cursebyte.plugin.modules.economy.EconomyService;
import com.cursebyte.plugin.player.TransferManager;

import java.util.UUID;

public class ChatInputListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        Player sender = event.getPlayer();

        if (!TransferManager.isInSession(sender)) {
            return;
        }

        event.setCancelled(true);

        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        if (message.equalsIgnoreCase("batal") || message.equalsIgnoreCase("cancel")) {
            sender.sendMessage("§cTransaksi dibatalkan.");
            TransferManager.endSession(sender);
            return;
        }

        try {
            double amount = Double.parseDouble(message);

            if (amount <= 0) {
                sender.sendMessage("§cJumlah harus lebih dari 0! Masukkan lagi:");
                return;
            }

            double balance = EconomyService.getBalance(sender.getUniqueId());
            if (!EconomyService.hasEnough(sender.getUniqueId(), amount)) {
                sender.sendMessage("§cUang kamu tidak cukup! (Saldo: ⛁ " + balance + ")");
                sender.sendMessage("§cTransaksi dibatalkan.");
                TransferManager.endSession(sender);
                return;
            }

            UUID targetId = TransferManager.getTarget(sender);
            boolean success = EconomyService.transfer(
                sender.getUniqueId(),
                targetId,
                amount
            );

            if (success) {
                Player targetPlayer = Bukkit.getPlayer(targetId);
                sender.sendMessage("§fBerhasil kirim §e⛁ " + amount + " §fke §e" + targetPlayer.getName());
                sender.playSound(sender.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

                targetPlayer.sendActionBar(
                    Component.text(
                        "Kamu menerima: ⛁ " + amount + " dari " + sender.getName(),
                        NamedTextColor.WHITE,
                        TextDecoration.BOLD
                    )
                );
                targetPlayer.playSound(
                    targetPlayer.getLocation(),
                    org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
                    1f,
                    1f
                );
            }else{
                sender.sendMessage("§cTransaksi Gagal: Saldo tidak mencukupi!");
            }

            TransferManager.endSession(sender);
            TransactionSession.activeTargets.remove(sender.getUniqueId());
        } catch (NumberFormatException e) {
            sender.sendMessage("§cItu bukan angka valid! Coba ketik angka saja (contoh: 5000).");
        }
    }
}