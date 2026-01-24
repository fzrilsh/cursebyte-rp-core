package com.cursebyte.plugin.ui.core;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface Menu {
    String id();
    Inventory build(Player player, MenuContext ctx);
    void onClick(Player player, int slot, MenuContext ctx);
}