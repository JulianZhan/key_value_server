package com.keyvalueserver.project.backup_support;

import com.keyvalueserver.project.keyvalue.KeyValuePair;

public interface BackupOperationFactory {
    BackupOperation createBackupOperation(KeyValuePair keyValuePair, OperationType operationType);
}
