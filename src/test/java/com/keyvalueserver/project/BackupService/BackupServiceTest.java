package com.keyvalueserver.project.BackupService;

import com.keyvalueserver.project.model.BackupOperation;
import com.keyvalueserver.project.model.KeyValuePair;
import com.keyvalueserver.project.repository.KeyValueRepository;
import com.keyvalueserver.project.service.BackupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class BackupServiceTest {

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
    void addToBackupQueueInsertOperation() throws InterruptedException {
        KeyValuePair pair = new KeyValuePair("key", "value");
        BackupOperation operation = new BackupOperation(true, pair);

        backupService.addToBackupQueue(operation);
//        Thread.sleep(1000); // Wait for the operation to be processed

        verify(keyValueRepository, timeout(1000)).insertOrUpdateKeyValue(pair);
    }

    @Test
    void addToBackupQueueDeleteOperation() throws InterruptedException {
        KeyValuePair pair = new KeyValuePair("key", null);
        BackupOperation operation = new BackupOperation(false, pair);

        backupService.addToBackupQueue(operation);
        Thread.sleep(1000); // Wait for the operation to be processed

        verify(keyValueRepository, timeout(1000)).deleteKeyValue("key");
    }
}

