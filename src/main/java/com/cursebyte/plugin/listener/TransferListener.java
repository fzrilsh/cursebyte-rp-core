package com.cursebyte.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.cursebyte.plugin.CursebyteCore;
import com.cursebyte.plugin.modules.economy.transfer.TransferFlow;
import com.cursebyte.plugin.modules.economy.transfer.TransferSession;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class TransferListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        Player sender = event.getPlayer();

        if (!TransferSession.has(sender.getUniqueId()))
            return;

        event.setCancelled(true);

        String msg = PlainTextComponentSerializer.plainText().serialize(event.message());

        Bukkit.getScheduler().runTask(CursebyteCore.getInstance(), () -> {
            if (msg.equalsIgnoreCase("batal") || msg.equalsIgnoreCase("cancel")) {
                TransferFlow.cancel(sender);
                return;
            }

            TransferFlow.process(sender, msg);
        });
    }
}
