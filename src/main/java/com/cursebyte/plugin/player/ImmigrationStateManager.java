package com.cursebyte.plugin.player;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class ImmigrationStateManager {
    private static final HashMap<UUID, String> sessions = new HashMap<>();

    public static void startSession(Player player, String sessionName) {
        sessions.put(player.getUniqueId(), sessionName);
    }

    public static boolean isInSession(Player player) {
        return sessions.containsKey(player.getUniqueId());
    }

    public static String getSession(Player player) {
        return sessions.get(player.getUniqueId());
    }

    public static void endSession(Player player) {
        sessions.remove(player.getUniqueId());
    }
}