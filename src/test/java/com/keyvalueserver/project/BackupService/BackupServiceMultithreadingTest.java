package com.keyvalueserver.project.BackupService;

import com.keyvalueserver.project.backup_support.BackupOperation;
import com.keyvalueserver.project.backup_support.BackupOperationFactory;
import com.keyvalueserver.project.backup_support.OperationType;
import com.keyvalueserver.project.backup_support.SimpleBackupOperationFactory;
import com.keyvalueserver.project.keyvalue.KeyValuePair;
import com.keyvalueserver.project.keyvalue.KeyValueRepository;
import com.keyvalueserver.project.backup_support.BackupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

class BackupServiceMultithreadingTest {

    @Mock
    private KeyValueRepository keyValueRepository;
    private BackupService backupService;
    private BackupOperationFactory backupOperationFactory;
    private int numThreads;
    private int numIterations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        backupService = new BackupService(keyValueRepository);
        backupOperationFactory = new SimpleBackupOperationFactory();
        numThreads = 100;
        numIterations = 100000;
    }

    @Test
    void testBackupServiceUnderLoad() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numIterations; i++) {
            executor.submit(() -> {
                KeyValuePair pair = new KeyValuePair("key", "value");
                // add insert and delete operations for both numIterations times to the backup queue
                BackupOperation operation = backupOperationFactory.createBackupOperation(pair, OperationType.INSERT);
                backupService.addToBackupQueue(operation);
                operation = backupOperationFactory.createBackupOperation(pair, OperationType.DELETE);
//                operation.delete
                backupService.addToBackupQueue(operation);
            });
        }

        // shutdown the executor and wait one second for all threads to finish
        executor.shutdown();

        // verify that insertOrUpdateKeyValue and deleteKeyValue were called numIterations times
        verify(keyValueRepository, timeout(2000).times(numIterations)).insertOrUpdateKeyValue(any(KeyValuePair.class));
        verify(keyValueRepository, timeout(2000).times(numIterations)).deleteKeyValue(any(String.class));
    }
}
