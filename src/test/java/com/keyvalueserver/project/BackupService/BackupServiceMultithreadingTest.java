package com.keyvalueserver.project.BackupService;

import com.keyvalueserver.project.model.BackupOperation;
import com.keyvalueserver.project.model.KeyValuePair;
import com.keyvalueserver.project.repository.KeyValueRepository;
import com.keyvalueserver.project.service.BackupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

class BackupServiceMultithreadingTest {

    @Mock
    private KeyValueRepository keyValueRepository;
    private BackupService backupService;
    private int numThreads;
    private int numIterations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        backupService = new BackupService(keyValueRepository);
        // start the backup service
        backupService.start();
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
                BackupOperation operation = new BackupOperation(true, pair);
                backupService.addToBackupQueue(operation);
                operation = new BackupOperation(false, pair);
                backupService.addToBackupQueue(operation);
            });
        }

        // shutdown the executor and wait one second for all threads to finish
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        // verify that insertOrUpdateKeyValue and deleteKeyValue were called numIterations times
        verify(keyValueRepository, timeout(1000).times(numIterations)).insertOrUpdateKeyValue(any(KeyValuePair.class));
        verify(keyValueRepository, timeout(1000).times(numIterations)).deleteKeyValue(any(String.class));
    }
}
