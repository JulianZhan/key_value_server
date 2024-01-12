package com.keyvalueserver.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BackupOperation {
    /*
    define custom class to represent the backup operation
    if isInsert is true, then the keyValuePair is an insert operation, otherwise it is a delete operation
    keyValuePair is the key value pair to be inserted or deleted
     */
    private final boolean isInsert;
    private final KeyValuePair keyValuePair;
}
