package com.cursebyte.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuRouter;

public class TransactionListener implements Listener {
    @EventHandler
    public void onRightClickPlayer(PlayerInteractEntityEvent e){
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (!(e.getRightClicked() instanceof Player)) return;

        Player sourcePlayer = e.getPlayer();
        Player targetPlayer = (Player) e.getRightClicked();

        MenuContext nctx = new MenuContext();
        nctx.set("target", targetPlayer.getUniqueId());

        MenuRouter.open(sourcePlayer, "transfer", nctx);
    }
}
