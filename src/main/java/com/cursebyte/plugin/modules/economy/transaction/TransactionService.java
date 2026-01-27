package com.cursebyte.plugin.modules.economy.transaction;

import java.util.*;

public class TransactionService {

    public static void log(UUID sender, UUID receiver, double amount, String desc) {
        TransactionRepository.addLog(sender, receiver, amount, desc);
    }

    public static List<TransactionRecord> getHistory(UUID uuid, int page, int size) {
        return TransactionRepository.getMutation(uuid, page, size);
    }
}