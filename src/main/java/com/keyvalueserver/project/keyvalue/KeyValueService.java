package com.keyvalueserver.project.keyvalue;

import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.keyvalueserver.project.backup_support.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.keyvalueserver.project.exceptions_support.KeyNotFoundException;
import java.io.IOException;
import java.util.Map;
import com.keyvalueserver.project.exceptions_support.ErrorMessage;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class KeyValueService {

    private final ConcurrentHashMap<String, String> keyValueStore = new ConcurrentHashMap<>();
    private final BackupService backupService;
    private final BackupOperationFactory backupOperationFactory;
    private final BackupRetrievalService backupRetrievalService;

    @Autowired
    public KeyValueService(BackupService backupService, BackupOperationFactory backupOperationFactory,
                           BackupRetrievalService backupRetrievalService) {
        this.backupService = backupService;
        this.backupOperationFactory = backupOperationFactory;
        this.backupRetrievalService = backupRetrievalService;
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
            // add backup operation to the queue and end the request to send ack to client
            // TODO: send ack after db backup
            // TODO: be careful with chaining
            BackupOperation operation = backupOperationFactory.createBackupOperation(keyValuePair, OperationType.INSERT);
            CompletableFuture<Void> completableFuture = backupService.addToBackupQueue(operation);
            completableFuture.join();
        }
    }

    public String[] getKeyValue(String[] keys) throws KeyNotFoundException, IllegalArgumentException {
        String[] values = new String[keys.length];
        // use for loop to iterate through keys array and get values from keyValueStore
        for (int i = 0; i < keys.length; i++) {
            // if key or value is null, throw exception, which will be handled by GlobalExceptionHandler later
            // REMINDER: consider reversing position
            if (keys[i] == null) {
                throw new IllegalArgumentException(ErrorMessage.KEY_OR_VALUE_CANNOT_BE_NULL);
            }
            String key = keys[i];
            values[i] = getValueForKey(key);
        }
        return values;
    }

    private String getValueForKey(String key) throws KeyNotFoundException {
        // TODO: unify interface for backup operations
        Optional<String> optionalValue = Optional.ofNullable(keyValueStore.get(key));
        if (optionalValue.isPresent()) {
            return optionalValue.get();
        }
        else {
            optionalValue = backupRetrievalService.getKeyValue(key);
            if (optionalValue.isPresent()) {
                String value = optionalValue.get();
                keyValueStore.put(key, value);
                return value;
            } else {
                throw new KeyNotFoundException(String.format(ErrorMessage.KEY_NOT_FOUND, key));
            }
        }
    }

    public void deleteKeyValue(String[] keys) throws IllegalArgumentException {
        for (String key : keys) {
            if (key == null) {
                throw new IllegalArgumentException(ErrorMessage.KEY_OR_VALUE_CANNOT_BE_NULL);
            }
            keyValueStore.remove(key);
            KeyValuePair keyValuePair = new KeyValuePair(key, null);
            BackupOperation operation = backupOperationFactory.createBackupOperation(keyValuePair, OperationType.DELETE);
            CompletableFuture<Void> completableFuture = backupService.addToBackupQueue(operation);
            completableFuture.join();
        }
    }

    public void exportCSVFile(PrintWriter writer) throws IOException {
        // Reference: https://springhow.com/spring-boot-export-to-csv/
        // receive PrintWriter from HttpServletResponse in KeyValueController
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT))
            {   // print header
                csvPrinter.printRecord("Key", "Value");
                // use for loop to print each key-value pair
                for (Map.Entry<String, String> entry : keyValueStore.entrySet()) {
                    csvPrinter.printRecord(entry.getKey(), entry.getValue());
            }
        }
    }
}
