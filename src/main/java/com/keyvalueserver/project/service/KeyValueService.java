package com.keyvalueserver.project.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import java.util.List;
import com.keyvalueserver.project.model.KeyValuePair;
import com.keyvalueserver.project.exceptions.KeyNotFoundException;

@Service
public class KeyValueService {

    private final ConcurrentHashMap<String, String> keyValueStore = new ConcurrentHashMap<>();

    public String setKeyValue(List<KeyValuePair> data) throws IllegalArgumentException {

        for (KeyValuePair keyValuePair : data) {
            String key = keyValuePair.getKey();
            String value = keyValuePair.getValue();
            if (key == null || value == null) {
                throw new IllegalArgumentException("Key or value cannot be null");
            }
            keyValueStore.put(key, value);
        }
        return "Key value pair added";
    }

    public String[] getKeyValue(String[] keys) throws KeyNotFoundException, IllegalArgumentException {
        String[] values = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }
            String value = keyValueStore.get(keys[i]);
            if (value == null) {
                throw new KeyNotFoundException(String.format("Key %s not found", keys[i]));
            }
            values[i] = value;
        }
        return values;
    }

    public String deleteKeyValue(String[] keys) throws IllegalArgumentException {
        for (String key : keys) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }
            keyValueStore.remove(key);
        }
        return "Key value pair deleted";
    }
}
