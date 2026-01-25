package com.cursebyte.plugin.modules.economy;

import java.util.UUID;

public class EconomyService {

    public static void initPlayer(UUID uuid) {
        EconomyRepository.createPlayer(uuid);
    }

    public static double getBalance(UUID uuid) {
        return EconomyRepository.getBalance(uuid);
    }

    public static boolean hasEnough(UUID uuid, double amount) {
        return getBalance(uuid) >= amount;
    }

    public static void add(UUID uuid, double amount) {
        EconomyRepository.add(uuid, amount);
    }

    public static boolean remove(UUID uuid, double amount) {
        if (!hasEnough(uuid, amount))
            return false;
        EconomyRepository.remove(uuid, amount);
        return true;
    }

    public static boolean transfer(UUID from, UUID to, double amount) {
        double tax = TaxService.calculateTax(amount);
        double total = amount + tax;

        if (!hasEnough(from, total)) {
            return false;
        }

        remove(from, total);
        add(to, amount);

        if (tax > 0) {
            // TODO: masukin ke kas negara
        }

        TransactionService.log(
                from,
                to,
                total,
                "TRANSFER + TAX: ");

        return true;
    }
}