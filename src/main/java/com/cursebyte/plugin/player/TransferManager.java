package com.cursebyte.plugin.player;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class TransferManager {
    private static final HashMap<UUID, UUID> pendingTransfers = new HashMap<>();

    public static void startTransferSession(Player sender, Player target) {
        pendingTransfers.put(sender.getUniqueId(), target.getUniqueId());
    }

    public static boolean isInSession(Player sender) {
        return pendingTransfers.containsKey(sender.getUniqueId());
    }

    public static UUID getTarget(Player sender) {
        return pendingTransfers.get(sender.getUniqueId());
    }

    public static void endSession(Player sender) {
        pendingTransfers.remove(sender.getUniqueId());
    }
}