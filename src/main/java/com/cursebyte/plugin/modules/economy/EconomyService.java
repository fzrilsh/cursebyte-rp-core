package com.cursebyte.plugin.modules.economy;

import java.util.UUID;

public class EconomyService {

    public static void initPlayer(UUID uuid){
        EconomyRepository.createPlayer(uuid);
    }

    public static double getBalance(UUID uuid){
        return EconomyRepository.getBalance(uuid);
    }

    public static boolean hasEnough(UUID uuid, double amount){
        return getBalance(uuid) >= amount;
    }

    public static void add(UUID uuid, double amount){
        EconomyRepository.add(uuid, amount);
    }

    public static boolean remove(UUID uuid, double amount){
        if(!hasEnough(uuid, amount)) return false;
        EconomyRepository.remove(uuid, amount);
        return true;
    }

    public static boolean transfer(UUID from, UUID to, double amount){
        if(!hasEnough(from, amount)) return false;

        EconomyRepository.remove(from, amount);
        EconomyRepository.add(to, amount);

        TransactionService.log(from, to, amount, "TRANSFER");
        return true;
    }
}