package com.keyvalueserver.project.backup_support;

import com.keyvalueserver.project.keyvalue.KeyValuePair;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BackupOperation {
    /*
    define custom class to represent the backup operation
    if isInsert is true, then the keyValuePair is an insert operation, otherwise it is a delete operation
    keyValuePair is the key value pair to be inserted or deleted
     */
    @Setter
    private OperationType operationType;
    private KeyValuePair keyValuePair;

    public BackupOperation(KeyValuePair keyValuePair) {
        this.keyValuePair = keyValuePair;
    }

    private BackupOperation() {
    };
}
