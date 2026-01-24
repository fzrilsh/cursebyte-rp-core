package com.cursebyte.plugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.cursebyte.plugin.economy.EconomyManager;

public class EconomyListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        EconomyManager.createPlayer(e.getPlayer().getUniqueId());
    }
}
