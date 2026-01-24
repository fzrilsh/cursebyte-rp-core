package com.cursebyte.plugin.modules.economy;

public record TransactionRecord(
        String sender,
        String receiver,
        double amount,
        long time,
        String description
) {}