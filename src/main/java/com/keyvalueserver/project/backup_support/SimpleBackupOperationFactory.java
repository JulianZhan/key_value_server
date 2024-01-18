package com.keyvalueserver.project.backup_support;

import com.keyvalueserver.project.keyvalue.KeyValuePair;
import org.springframework.stereotype.Component;

@Component
public class SimpleBackupOperationFactory implements BackupOperationFactory {

    @Override
    public BackupOperation createBackupOperation(KeyValuePair keyValuePair, OperationType operationType) {
        BackupOperation operation = new BackupOperation(keyValuePair);
        operation.setOperationType(operationType);
        return operation;
    }
}
