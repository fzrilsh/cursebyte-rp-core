package com.cursebyte.plugin.ui.core;

import java.util.*;

import org.bukkit.entity.Player;

public class MenuSession {
    private static final Map<UUID, String> activeMenu = new HashMap<>();
    private static final Map<UUID, MenuContext> contexts = new HashMap<>();

    public static void set(Player p, String menuId, MenuContext ctx){
        activeMenu.put(p.getUniqueId(), menuId);
        contexts.put(p.getUniqueId(), ctx);
    }

    public static String getMenu(Player p){
        return activeMenu.get(p.getUniqueId());
    }

    public static MenuContext getContext(Player p){
        return contexts.get(p.getUniqueId());
    }

    public static void clear(Player p){
        activeMenu.remove(p.getUniqueId());
        contexts.remove(p.getUniqueId());
    }
}