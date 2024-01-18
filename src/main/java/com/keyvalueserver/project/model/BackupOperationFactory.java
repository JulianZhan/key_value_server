package com.keyvalueserver.project.model;

public interface BackupOperationFactory {
    BackupOperation createBackupOperation(KeyValuePair keyValuePair, OperationType operationType);
}
