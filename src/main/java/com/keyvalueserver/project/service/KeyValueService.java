package com.keyvalueserver.project.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import java.util.List;
import com.keyvalueserver.project.model.KeyValuePair;
import com.keyvalueserver.project.exceptions.KeyNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.io.Writer;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import lombok.extern.slf4j.Slf4j;


@Slf4j
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

    public void exportCSVFile(Writer writer) throws IOException {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Key", "Value");
            for (Map.Entry<String, String> entry : keyValueStore.entrySet()) {
                csvPrinter.printRecord(entry.getKey(), entry.getValue());
            }
        } catch (IOException e) {
            log.error("Error writing to CSV file");
            throw e;
        }
    }
}
