package com.cursebyte.plugin.modules.citizen;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class CitizenSessionManager {

    private static final HashMap<UUID, String> sessions = new HashMap<>();

    public static void start(Player player, String state) {
        sessions.put(player.getUniqueId(), state);
    }

    public static boolean isActive(Player player) {
        return sessions.containsKey(player.getUniqueId());
    }

    public static String get(Player player) {
        return sessions.get(player.getUniqueId());
    }

    public static void end(Player player) {
        sessions.remove(player.getUniqueId());
    }
}