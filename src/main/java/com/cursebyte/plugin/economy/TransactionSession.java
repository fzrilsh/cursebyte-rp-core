package com.cursebyte.plugin.economy;

import java.util.HashMap;
import java.util.UUID;

public class TransactionSession {
    public static HashMap<UUID, UUID> activeTargets = new HashMap<>();
}