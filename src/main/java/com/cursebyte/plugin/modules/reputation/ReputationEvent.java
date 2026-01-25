package com.cursebyte.plugin.modules.reputation;

import java.util.Map;
import java.util.UUID;

public class ReputationEvent {

    private final UUID playerId;
    private final ReputationEventType type;
    private final Map<String, Object> data;

    public ReputationEvent(UUID playerId, ReputationEventType type, Map<String, Object> data){
        this.playerId = playerId;
        this.type = type;
        this.data = data;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public ReputationEventType getType() {
        return type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz){
        return (T) data.get(key);
    }
}