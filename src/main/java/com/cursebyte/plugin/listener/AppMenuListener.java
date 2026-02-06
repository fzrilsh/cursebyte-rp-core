package com.cursebyte.plugin.listener;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuRegistry;
import com.cursebyte.plugin.ui.core.MenuSession;

public class AppMenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p))
            return;

        if (e.getClickedInventory() != e.getView().getTopInventory())
            return;

        MenuContext session = MenuSession.getContext(p);
        if (session == null)
            return;

        String menuId = MenuSession.getMenu(p);
        if (menuId == null)
            return;

        Menu menu = MenuRegistry.get(menuId);
        if (menu == null)
            return;

        int rawSlot = e.getRawSlot();
        Set<Integer> inputSlots = getInputSlots(session);
        boolean isInput = inputSlots.contains(rawSlot);

        e.setCancelled(true);

        if (isInput) {
            if (isBypassAction(e)) {
                e.setCancelled(true);
                return;
            }

            switch (e.getAction()) {
                case PLACE_ALL, PLACE_ONE, PLACE_SOME,
                        PICKUP_ALL, PICKUP_HALF, PICKUP_ONE, PICKUP_SOME,
                        SWAP_WITH_CURSOR -> {
                    e.setCancelled(false);
                    menu.onChange(p, rawSlot, session);
                    return;
                }

                default -> {
                    e.setCancelled(true);
                    return;
                }
            }
        }

        menu.onClick(p, e.getSlot(), MenuSession.getContext(p));
    }

    @EventHandler
    public void onMenuDrag(InventoryDragEvent e) {
        if (!(e.getWhoClicked() instanceof Player p))
            return;

        MenuContext session = MenuSession.getContext(p);
        if (session == null)
            return;

        String menuId = MenuSession.getMenu(p);
        if (menuId == null)
            return;

        Menu menu = MenuRegistry.get(menuId);
        if (menu == null)
            return;

        @SuppressWarnings("unchecked")
        Set<Integer> inputSlots = getInputSlots(MenuSession.getContext(p));
        int topSize = e.getView().getTopInventory().getSize();

        for (int rawSlot : e.getRawSlots()) {
            if (rawSlot < topSize) {
                boolean isInput = inputSlots.contains(rawSlot);

                if (!isInput) {
                    e.setCancelled(true);
                    return;
                }
            }
        }

        e.setCancelled(false);
    }

    @SuppressWarnings("unchecked")
    private Set<Integer> getInputSlots(MenuContext ctx) {
        Object o = ctx.get("inputSlots", Object.class);
        if (o instanceof Set<?> set) {
            Set<Integer> out = new HashSet<>();
            for (Object x : set)
                if (x instanceof Integer i)
                    out.add(i);
            return out;
        }

        return Set.of();
    }

    private boolean isBypassAction(InventoryClickEvent e) {
        InventoryAction a = e.getAction();

        if (e.getClick().isShiftClick())
            return true;

        if (e.getClick() == ClickType.NUMBER_KEY)
            return true;

        if (a == InventoryAction.COLLECT_TO_CURSOR)
            return true;

        if (a == InventoryAction.DROP_ALL_SLOT || a == InventoryAction.DROP_ONE_SLOT)
            return true;

        return false;
    }
}
