package com.cursebyte.plugin.ui.core;

import java.util.HashMap;
import java.util.Map;

public class MenuRegistry {
    private static final Map<String, Menu> MENUS = new HashMap<>();

    public static void register(Menu menu){
        MENUS.put(menu.id(), menu);
    }

    public static Menu get(String id){
        return MENUS.get(id);
    }
}