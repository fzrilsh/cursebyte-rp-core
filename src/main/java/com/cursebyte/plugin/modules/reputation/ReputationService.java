package com.cursebyte.plugin.modules.reputation;

import java.util.UUID;

import com.cursebyte.plugin.CursebyteCore;

public class ReputationService {

    public static void initCitizen(UUID uuid){
        ReputationRepository.create(uuid, CursebyteCore.getInstance().getConfig().getDouble("reputation.default", 0.5));
    }

    public static double get(UUID uuid){
        return ReputationRepository.get(uuid);
    }

    public static void add(UUID uuid, double delta){
        double current = get(uuid);
        double updated = clamp(current + delta);
        ReputationRepository.set(uuid, updated);
    }

    public static void subtract(UUID uuid, double delta){
        double current = get(uuid);
        double updated = clamp(current - delta);
        ReputationRepository.set(uuid, updated);
    }

    public static boolean isEligible(UUID uuid, double required){
        return get(uuid) >= required;
    }

    private static double clamp(double v){
        if(v < 0.0) return 0.0;
        if(v > 1.0) return 1.0;
        return v;
    }
}