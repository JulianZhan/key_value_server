package com.keyvalueserver.project.model;

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
