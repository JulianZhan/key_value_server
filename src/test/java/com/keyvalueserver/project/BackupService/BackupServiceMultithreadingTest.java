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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        backupService = new BackupService(keyValueRepository);
        backupService.start();
    }

    @Test
    void testBackupServiceUnderLoad() throws InterruptedException {
        int numberOfThreads = 10; // Number of concurrent threads
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                // Simulate an operation, e.g., insert or delete
                KeyValuePair pair = new KeyValuePair("key", "value");
                BackupOperation operation = new BackupOperation(true, pair);
                backupService.addToBackupQueue(operation);
            });
        }

        // Shutdown executor and wait for tasks to complete
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        // Verify that the method was called the expected number of times
        verify(keyValueRepository, timeout(1000).times(numberOfThreads)).insertOrUpdateKeyValue(any(KeyValuePair.class));
    }
}
