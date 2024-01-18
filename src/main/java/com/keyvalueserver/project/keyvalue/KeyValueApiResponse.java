package com.keyvalueserver.project.keyvalue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyValueApiResponse {
    private final boolean success;
    private final String message;
    private final String[] data;
}
