package com.cursebyte.plugin.modules.economy.transfer;

import java.util.HashMap;
import java.util.UUID;

public class TransferSession {

    private static final HashMap<UUID, UUID> sessions = new HashMap<>();

    public static void start(UUID sender, UUID target) {
        sessions.put(sender, target);
    }

    public static boolean has(UUID sender) {
        return sessions.containsKey(sender);
    }

    public static UUID getTarget(UUID sender) {
        return sessions.get(sender);
    }

    public static void end(UUID sender) {
        sessions.remove(sender);
    }
}