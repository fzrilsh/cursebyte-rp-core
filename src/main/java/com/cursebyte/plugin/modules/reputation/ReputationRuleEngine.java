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
}