package com.cursebyte.plugin.modules.economy.transfer;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.economy.TaxService;
import com.cursebyte.plugin.modules.economy.transfer.TransferService.TransferResult;
import com.cursebyte.plugin.modules.reputation.ReputationEvent;
import com.cursebyte.plugin.modules.reputation.ReputationEventBus;
import com.cursebyte.plugin.modules.reputation.ReputationEventType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TransferFlow {

    public static void start(Player sender, Player target) {
        TransferSession.start(sender.getUniqueId(), target.getUniqueId());

        sender.sendMessage("§e========================================");
        sender.sendMessage("§eSedang mentransfer ke: §f" + target.getName());
        sender.sendMessage("§aSilakan ketik jumlah uang di chat.");
        sender.sendMessage("§7(Ketik 'batal' untuk membatalkan)");
        sender.sendMessage("§e========================================");
    }

    public static void cancel(Player sender) {
        TransferSession.end(sender.getUniqueId());
        sender.sendMessage("§cTransaksi dibatalkan.");
    }

    public static void process(Player sender, String input) {
        UUID senderId = sender.getUniqueId();
        UUID targetId = TransferSession.getTarget(senderId);

        double amount;
        try {
            amount = Double.parseDouble(input);
        } catch (Exception e) {
            sender.sendMessage("§cInput tidak valid. Masukkan angka!");
            return;
        }

        TransferResult result = TransferService.transfer(senderId, targetId, amount);
        Player target = Bukkit.getPlayer(targetId);

        switch (result) {
            case INVALID_AMOUNT -> sender.sendMessage("§cJumlah harus lebih dari 0!");
            case INSUFFICIENT_BALANCE -> sender.sendMessage("§cSaldo tidak cukup!");
            case FAILED -> sender.sendMessage("§cTransaksi gagal!");
            case SUCCESS -> {
                sender.sendMessage("§fBerhasil kirim §e⛁ " + amount + " §fke §e" + target.getName() + " §fdengan pajak §e⛁ " + TaxService.calculateTax(amount));
                sender.playSound(sender.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

                ReputationEventBus.emit(new ReputationEvent(
                    sender.getUniqueId(),
                    ReputationEventType.TRANSFER,
                    Map.of()
                ));

                if (target != null) {
                    target.sendActionBar(Component.text(
                        "Kamu menerima: ⛁ " + amount + " dari " + sender.getName(),
                        NamedTextColor.GREEN
                    ));
                    target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                }

                TransferSession.end(senderId);
            }
        }
    }
}