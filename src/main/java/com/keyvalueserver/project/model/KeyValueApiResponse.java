package com.keyvalueserver.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyValueApiResponse {
    private final boolean success;
    private final String message;
    private final String[] data;
}