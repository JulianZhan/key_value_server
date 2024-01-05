package com.keyvalueserver.project.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class KeyValuePair {
    private final String key;
    private final String value;
}
