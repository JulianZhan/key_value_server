package com.keyvalueserver.project.model;

import org.springframework.stereotype.Component;

@Component("insertBackupOperationFactory")
public class InsertBackupOperationFactory implements BackupOperationFactory {
    @Override
    public BackupOperation createBackupOperation(KeyValuePair keyValuePair) {
        BackupOperation operation = new BackupOperation(keyValuePair);
        operation.setOperationType(OperationType.INSERT);
        return operation;
    }
}

