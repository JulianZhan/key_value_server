package com.keyvalueserver.project.KeyValueService;

import com.keyvalueserver.project.backup_support.BackupOperation;
import com.keyvalueserver.project.backup_support.BackupOperationFactory;
import com.keyvalueserver.project.backup_support.OperationType;
import com.keyvalueserver.project.backup_support.BackupRetrievalService;
import com.keyvalueserver.project.backup_support.BackupService;
import com.keyvalueserver.project.keyvalue.KeyValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.keyvalueserver.project.keyvalue.KeyValuePair;
import com.keyvalueserver.project.exceptions_support.KeyNotFoundException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class KeyValueServiceTest {

    @Mock
    private BackupService backupService;
    @Mock
    private BackupOperationFactory SimpleBackupOperationFactory;
    @Mock
    private BackupRetrievalService backupRetrievalService;

    private KeyValueService keyValueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        BackupOperation mockBackupOperation = new BackupOperation(new KeyValuePair("test", "test"));
        mockBackupOperation.setOperationType(OperationType.INSERT);

        when(SimpleBackupOperationFactory.createBackupOperation(any(KeyValuePair.class), any(OperationType.class)))
                .thenReturn(mockBackupOperation);
        CompletableFuture<Void> completedFuture = CompletableFuture.completedFuture(null);
        when(backupService.addToBackupQueue(eq(mockBackupOperation))).thenReturn(completedFuture);

        keyValueService = new KeyValueService(backupService, SimpleBackupOperationFactory, backupRetrievalService);
    }

    @Test
    void testSetAndGetKeyValue() {
        List<KeyValuePair> keyValuePairs = new ArrayList<>();
        keyValuePairs.add(new KeyValuePair("key1", "value1"));
        keyValuePairs.add(new KeyValuePair("key2", "value2"));
        keyValueService.setKeyValue(keyValuePairs);

        String[] keys = {"key1", "key2"};
        String[] values = keyValueService.getKeyValue(keys);

        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test
    void testKeyNotFound() {
        String[] keys = {"nonExistingKey"};
        KeyNotFoundException exception = assertThrows(
                KeyNotFoundException.class,
                () -> keyValueService.getKeyValue(keys)
        );

        assertEquals("Key nonExistingKey not found", exception.getMessage());
    }

    @Test
    void testNullKeyInSet() {
        List<KeyValuePair> keyValuePairs = new ArrayList<>();
        keyValuePairs.add(new KeyValuePair(null, "value1"));
        assertThrows(IllegalArgumentException.class, () -> keyValueService.setKeyValue(keyValuePairs));
    }

    @Test
    void testNullValueInSet() {
        List<KeyValuePair> keyValuePairs = new ArrayList<>();
        keyValuePairs.add(new KeyValuePair("key1", null));
        assertThrows(IllegalArgumentException.class, () -> keyValueService.setKeyValue(keyValuePairs));
    }

    @Test
    void testNullKeyInGet() {
        String[] keys = {null};
        assertThrows(IllegalArgumentException.class, () -> keyValueService.getKeyValue(keys));
    }

    @Test
    void testDeleteKeyValue() {
        // Setting a key-value pair
        List<KeyValuePair> keyValuePairs = new ArrayList<>();
        keyValuePairs.add(new KeyValuePair("key1", "value1"));
        keyValueService.setKeyValue(keyValuePairs);

        // Deleting the key-value pair
        String[] keysToDelete = {"key1"};
        keyValueService.deleteKeyValue(keysToDelete);

        // Trying to retrieve deleted key
        String[] keys = {"key1"};
        assertThrows(KeyNotFoundException.class, () -> keyValueService.getKeyValue(keys));
    }

    @Test
    void testNullKeyInDelete() {
        String[] keys = {null};
        assertThrows(IllegalArgumentException.class, () -> keyValueService.deleteKeyValue(keys));
    }

}
