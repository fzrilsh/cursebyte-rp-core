package com.cursebyte.plugin.ui.core;

import java.util.HashMap;
import java.util.Map;

public class MenuContext {
    private final Map<String, Object> data = new HashMap<>();

    public void set(String key, Object val){
        data.put(key, val);
    }

    public <T> T get(String key, Class<T> cls){
        return cls.cast(data.get(key));
    }
}