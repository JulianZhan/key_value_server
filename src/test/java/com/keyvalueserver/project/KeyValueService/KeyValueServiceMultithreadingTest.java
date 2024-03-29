package com.keyvalueserver.project.KeyValueService;

import com.keyvalueserver.project.backup_support.*;
import com.keyvalueserver.project.exceptions_support.KeyNotFoundException;
import com.keyvalueserver.project.keyvalue.KeyValuePOJO;
import com.keyvalueserver.project.keyvalue.KeyValuePair;
import com.keyvalueserver.project.keyvalue.KeyValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class KeyValueServiceMultithreadingTest {
    private KeyValueService keyValueService;
    @Mock
    private BackupService backupService;
    @Mock
    private BackupOperationFactory SimpleBackupOperationFactory;
    @Mock
    private BackupRetrievalService backupRetrievalService;
    private int numThreads;
    private int numIterations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        BackupOperation mockBackupOperation = new BackupOperation(new KeyValuePair("test", "test"));
        mockBackupOperation.setOperationType(OperationType.INSERT);

        when(SimpleBackupOperationFactory.createBackupOperation(any(KeyValuePair.class), any(OperationType.class)))
                .thenReturn(mockBackupOperation);

        keyValueService = new KeyValueService(backupService, SimpleBackupOperationFactory, backupRetrievalService);


        numThreads = 100;
        numIterations = 100000;
    }

    @Test
    void testConcurrentAccess() throws InterruptedException {
        // create a thread pool with numThreads threads
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numIterations; i++) {
            // create a KeyValuePOJO with a single KeyValuePair
            KeyValuePOJO keyValuePOJO = new KeyValuePOJO(List.of(new KeyValuePair("key" + i, "value" + i)));
            /*
             *submit a runnable task to the executor to set the key-value pair,
             *which will be executed by one of the threads in the thread pool
             */
            executor.submit(() -> {
                keyValueService.setKeyValue(keyValuePOJO.getData());
            });
        }

        for (int i = 0; i < numIterations; i++) {
            String[] keys = {"key" + i};
            int finalI = i;
            executor.submit(() -> {
                try {
                    String[] values = keyValueService.getKeyValue(keys, false);
                    assertEquals("value" + finalI, values[0]);
                    values = keyValueService.getKeyValue(keys, true);
                    assertEquals("value" + finalI, values[0]);
                } catch (KeyNotFoundException e) {
                    fail("Key not found: " + e.getMessage());
                }
            });
        }

        for (int i = 0; i < numIterations; i++) {
            String[] keys = {"key" + i};
            executor.submit(() -> {
                keyValueService.deleteKeyValue(keys);
            });
        }

        // shutdown the executor and check if all tasks have completed on time
        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));

        for (int i = 0; i < numIterations; i++) {
            String[] keys = {"key" + i};
            // after deleting the key-value pair, all the keys should not be found
            assertThrows(KeyNotFoundException.class, () -> keyValueService.getKeyValue(keys, false));
            assertThrows(KeyNotFoundException.class, () -> keyValueService.getKeyValue(keys, true));
        }
    }
}
