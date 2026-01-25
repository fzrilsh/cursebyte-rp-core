package com.cursebyte.plugin.modules.reputation;

import org.bukkit.configuration.file.FileConfiguration;
import com.cursebyte.plugin.CursebyteCore;

public class ReputationRuleEngine {

    public static void process(ReputationEvent event){

        FileConfiguration cfg = CursebyteCore.getInstance().getConfig();

        String path = "reputation.events." + event.getType().name();

        if(!cfg.getBoolean(path + ".enabled", false)){
            return;
        }

        double delta = cfg.getDouble(path + ".delta", 0.0);

        if(delta == 0.0) return;

        ReputationManager.applyDelta(event.getPlayerId(), delta);
    }

    // public static void process(ReputationEvent event) {
    //     switch (event.getType()) {
    //         case TRANSFER -> {
    //             double amount = event.get("amount", Double.class);
    //             if (amount >= 1000) {
    //                 ReputationManager.apply(event.getPlayerId(), ReputationAction.TRANSFER_SUCCESS);
    //             }
    //         }

    //         case JOB_COMPLETE -> {
    //             ReputationManager.apply(event.getPlayerId(), ReputationAction.JOB_COMPLETE);
    //         }

    //         case TAX_PAYMENT -> {
    //             ReputationManager.apply(event.getPlayerId(), ReputationAction.TAX_PAYMENT);
    //         }

    //         case PUBLIC_SERVICE -> {
    //             ReputationManager.apply(event.getPlayerId(), ReputationAction.PUBLIC_SERVICE);
    //         }

    //         case CRIME -> {
    //             ReputationManager.apply(event.getPlayerId(), ReputationAction.CRIME);
    //         }

    //         case SCAM -> {
    //             ReputationManager.apply(event.getPlayerId(), ReputationAction.SCAM);
    //         }

    //         case REPORT -> {
    //             boolean valid = event.get("valid", Boolean.class);
    //             if (valid) {
    //                 ReputationManager.apply(event.getPlayerId(), ReputationAction.REPORT_VALID);
    //             }
    //         }

    //         case SYSTEM_ABUSE -> {
    //             ReputationManager.apply(event.getPlayerId(), ReputationAction.ABUSE_SYSTEM);
    //         }
    //     }
    // }
}