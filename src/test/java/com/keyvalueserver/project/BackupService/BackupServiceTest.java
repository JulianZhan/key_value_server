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

import static org.mockito.Mockito.*;

class BackupServiceTest {

    @Mock
    private KeyValueRepository keyValueRepository;

    private BackupService backupService;
    private BackupOperationFactory backupOperationFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        backupService = new BackupService(keyValueRepository);
        backupOperationFactory = new SimpleBackupOperationFactory();
    }

    @Test
    void addToBackupQueueInsertOperation() throws InterruptedException {
        KeyValuePair pair = new KeyValuePair("key", "value");
        BackupOperation operation = backupOperationFactory.createBackupOperation(pair, OperationType.INSERT);
        backupService.addToBackupQueue(operation);
        verify(keyValueRepository, timeout(50)).insertOrUpdateKeyValue(pair);
    }

    @Test
    void addToBackupQueueDeleteOperation() throws InterruptedException {
        KeyValuePair pair = new KeyValuePair("key", null);
        BackupOperation operation = backupOperationFactory.createBackupOperation(pair, OperationType.DELETE);
        backupService.addToBackupQueue(operation);
        verify(keyValueRepository, timeout(50)).deleteKeyValue("key");
    }
}

