package com.keyvalueserver.project.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import java.util.List;
import com.keyvalueserver.project.model.KeyValuePair;
import com.keyvalueserver.project.exceptions.KeyNotFoundException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;


@Service
public class KeyValueService {

    private final ConcurrentHashMap<String, String> keyValueStore = new ConcurrentHashMap<>();

    public void setKeyValue(List<KeyValuePair> data) throws IllegalArgumentException {

        for (KeyValuePair keyValuePair : data) {
            String key = keyValuePair.getKey();
            String value = keyValuePair.getValue();
            if (key == null || value == null) {
                throw new IllegalArgumentException("Key or value cannot be null");
            }
            keyValueStore.put(key, value);
        }
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

    public void deleteKeyValue(String[] keys) throws IllegalArgumentException {
        for (String key : keys) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }
            keyValueStore.remove(key);
        }
    }

    public File createCSVFile() throws IOException {
        File csvFile = File.createTempFile("key-value-pairs", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            for (Map.Entry<String, String> entry : keyValueStore.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        }
        return csvFile;
    }
}
