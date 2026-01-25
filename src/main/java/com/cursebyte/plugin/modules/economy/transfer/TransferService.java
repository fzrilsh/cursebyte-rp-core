package com.cursebyte.plugin.modules.economy.transfer;

import java.util.UUID;

import com.cursebyte.plugin.modules.economy.EconomyService;
import com.cursebyte.plugin.modules.economy.TaxService;

public class TransferService {

    public enum TransferResult {
        SUCCESS,
        INVALID_AMOUNT,
        INSUFFICIENT_BALANCE,
        FAILED
    }

    public static TransferResult transfer(UUID sender, UUID target, double amount) {
        if (amount <= 0)
            return TransferResult.INVALID_AMOUNT;

        double tax = TaxService.calculateTax(amount);
        double total = amount + tax;

        if (!EconomyService.hasEnough(sender, total))
            return TransferResult.INSUFFICIENT_BALANCE;

        boolean success = EconomyService.transfer(sender, target, amount);

        if (!success)
            return TransferResult.FAILED;

        return TransferResult.SUCCESS;
    }
}
