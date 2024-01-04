package com.keyvalueserver.project;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class KeyValueService {

    private ConcurrentHashMap<String, String> keyValueStore = new ConcurrentHashMap<>();

    public String setKeyValue(String key, String value) {
        keyValueStore.put(key, value);
        return "Key value pair added";
    }

    public String getKeyValue(String key) {
        return keyValueStore.get(key);
    }

    public String deleteKeyValue(String key) {
        keyValueStore.remove(key);
        return "Key value pair deleted";
    }
}
