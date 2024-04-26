package com.api.component;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Cache {

    private Map<String, Object> hashMap;

    public Cache() {
        this.hashMap = new LinkedHashMap<>();
    }

    public Object get(String key) {
        return hashMap.get(key);
    }

    public void put(String key, Object obj) {
        hashMap.put(key, obj);
        if (hashMap.size() > 10) {
            String oldKey = hashMap.keySet().iterator().next();
            hashMap.remove(oldKey);
        }
    }

    public void remove(String key) {
        hashMap.remove(key);
    }

    public void clear() {
        hashMap.clear();
    }

}
