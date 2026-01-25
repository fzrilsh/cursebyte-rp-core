package com.cursebyte.plugin.modules.reputation;

import java.util.UUID;

import com.cursebyte.plugin.CursebyteCore;

public class ReputationManager {

    public static void init() {
        ReputationRepository.init();
    }

    public static double get(UUID uuid){
        return ReputationRepository.get(uuid);
    }

    public static void set(UUID uuid, double value){
        ReputationRepository.set(uuid, clamp(value));
    }

    public static void applyDelta(UUID uuid, double delta){
        double current = ReputationRepository.get(uuid);

        double min = CursebyteCore.getInstance().getConfig().getDouble("reputation.min", 0.0);
        double max = CursebyteCore.getInstance().getConfig().getDouble("reputation.max", 1.0);

        double updated = current + delta;

        if(updated < min) updated = min;
        if(updated > max) updated = max;

        ReputationRepository.set(uuid, updated);
    }

    private static double clamp(double v){
        if(v < 0.0) return 0.0;
        if(v > 1.0) return 1.0;
        return v;
    }

    public static boolean isEligible(UUID uuid, double required){
        return get(uuid) >= required;
    }

    public static String getStatus(UUID uuid){
        double r = get(uuid);

        if(r >= 0.9) return "§bWarga Terhormat";
        if(r >= 0.75) return "§aWarga Terpercaya";
        if(r >= 0.5) return "§fWarga Sipil";
        if(r >= 0.3) return "§6Warga Dalam Pengawasan";
        if(r >= 0.1) return "§cWarga Yang Berbahaya";
        return "§4§lBuronan Konstitusi";
    }
}