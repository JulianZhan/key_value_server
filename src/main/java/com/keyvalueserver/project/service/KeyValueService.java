package com.keyvalueserver.project.service;

import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import com.keyvalueserver.project.model.BackupOperation;
import com.keyvalueserver.project.repository.KeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.keyvalueserver.project.model.KeyValuePair;
import com.keyvalueserver.project.exceptions.KeyNotFoundException;
import java.io.IOException;
import java.util.Map;
import com.keyvalueserver.project.exceptions.ErrorMessage;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class KeyValueService {

    private final ConcurrentHashMap<String, String> keyValueStore = new ConcurrentHashMap<>();
    private final BackupService backupService;
    private final KeyValueRepository keyValueRepository;

    @Autowired
    public KeyValueService(BackupService backupService, KeyValueRepository keyValueRepository) {
        this.backupService = backupService;
        this.keyValueRepository = keyValueRepository;
    }


    public void setKeyValue(List<KeyValuePair> data) throws IllegalArgumentException {
        // use for each loop to iterate through data list and put key-value pairs into keyValueStore
        for (KeyValuePair keyValuePair : data) {
            String key = keyValuePair.getKey();
            String value = keyValuePair.getValue();
            // if key or value is null, throw exception, which will be handled by GlobalExceptionHandler later
            if (key == null || value == null) {
                throw new IllegalArgumentException(ErrorMessage.KEY_OR_VALUE_CANNOT_BE_NULL);
            }
            keyValueStore.put(key, value);
            backupService.addToBackupQueue(new BackupOperation(true, keyValuePair));
        }
    }

    public String[] getKeyValue(String[] keys) throws KeyNotFoundException, IllegalArgumentException {
        String[] values = new String[keys.length];
        // use for loop to iterate through keys array and get values from keyValueStore
        for (int i = 0; i < keys.length; i++) {
            // if key or value is null, throw exception, which will be handled by GlobalExceptionHandler later
            if (keys[i] == null) {
                throw new IllegalArgumentException(ErrorMessage.KEY_OR_VALUE_CANNOT_BE_NULL);
            }
            values[i] = getValueForKey(keys[i]);
        }
        return values;
    }

    private String getValueForKey(String key) throws KeyNotFoundException {
        String value = keyValueStore.get(key);
        if (value != null) {
            return value;
        }
        value = keyValueRepository.getKeyValue(key);
        if (value == null) {
            throw new KeyNotFoundException(String.format(ErrorMessage.KEY_NOT_FOUND, key));
        }
        // update cache
        keyValueStore.put(key, value);
        return value;
    }

    public void deleteKeyValue(String[] keys) throws IllegalArgumentException {
        for (String key : keys) {
            if (key == null) {
                throw new IllegalArgumentException(ErrorMessage.KEY_OR_VALUE_CANNOT_BE_NULL);
            }
            keyValueStore.remove(key);
            backupService.addToBackupQueue(new BackupOperation(false, new KeyValuePair(key, null)));
        }
    }

    public void exportCSVFile(PrintWriter writer) throws IOException {
        // Reference: https://springhow.com/spring-boot-export-to-csv/
        // receive PrintWriter from HttpServletResponse in KeyValueController
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        // print header
        csvPrinter.printRecord("Key", "Value");
        // use for loop to print each key-value pair
        for (Map.Entry<String, String> entry : keyValueStore.entrySet()) {
            csvPrinter.printRecord(entry.getKey(), entry.getValue());
        }
    }
}
