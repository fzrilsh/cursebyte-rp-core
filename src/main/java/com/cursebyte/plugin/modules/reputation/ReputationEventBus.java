package com.cursebyte.plugin.modules.reputation;

public class ReputationEventBus {

    public static void emit(ReputationEvent event){
        ReputationRuleEngine.process(event);
    }

}