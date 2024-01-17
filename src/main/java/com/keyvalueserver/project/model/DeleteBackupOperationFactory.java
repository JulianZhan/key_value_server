package com.keyvalueserver.project.model;

import org.springframework.stereotype.Component;

@Component("deleteBackupOperationFactory")
public class DeleteBackupOperationFactory implements BackupOperationFactory {

    @Override
    public BackupOperation createBackupOperation(KeyValuePair keyValuePair) {
        BackupOperation operation = new BackupOperation(keyValuePair);
        operation.setOperationType(OperationType.DELETE);
        return operation;
    }
}