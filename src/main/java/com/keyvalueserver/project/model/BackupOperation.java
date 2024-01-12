package com.keyvalueserver.project.model;

import com.keyvalueserver.project.model.KeyValuePair;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BackupOperation {
    private final boolean isInsert;
    private final KeyValuePair keyValuePair;
}
