package com.cursebyte.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuRegistry;
import com.cursebyte.plugin.ui.core.MenuSession;

public class AppMenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p))
            return;

        String menuId = MenuSession.getMenu(p);
        if (menuId == null)
            return;

        Menu menu = MenuRegistry.get(menuId);
        if (menu == null)
            return;

        e.setCancelled(true);
        menu.onClick(p, e.getSlot(), MenuSession.getContext(p));
    }
}
