package com.keyvalueserver.project.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import com.keyvalueserver.project.exceptions.KeyNotFoundException;

@Service
public class KeyValueService {

    private final ConcurrentHashMap<String, String> keyValueStore = new ConcurrentHashMap<>();

    public String setKeyValue(String key, String value) throws IllegalArgumentException {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }
        keyValueStore.put(key, value);
        return "Key value pair added";
    }

    public String getKeyValue(String key) throws KeyNotFoundException, IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        String value = keyValueStore.get(key);
        if (value == null) {
            throw new KeyNotFoundException(String.format("Key %s not found", key));
        }
        return value;
    }

    public String deleteKeyValue(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        String deletedValue = keyValueStore.remove(key);
        if (deletedValue == null) {
            return "Key value pair not found";
        }
        else {
            return "Key value pair deleted";
        }
    }
}
