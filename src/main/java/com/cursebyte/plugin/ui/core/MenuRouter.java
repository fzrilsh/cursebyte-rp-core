package com.cursebyte.plugin.ui.core;

import org.bukkit.entity.Player;

public class MenuRouter {

    public static void open(Player player, String menuId){
        open(player, menuId, new MenuContext());
    }

    public static void open(Player player, String menuId, MenuContext ctx){
        Menu menu = MenuRegistry.get(menuId);
        if(menu == null){
            player.sendMessage("§cMenu tidak ditemukan: " + menuId);
            return;
        }

        var inv = menu.build(player, ctx);
        player.openInventory(inv);

        MenuSession.set(player, menuId, ctx);
    }
}