package com.keyvalueserver.project.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.HashCodeExclude;

@AllArgsConstructor
@Getter
@ToString
public class KeyValuePOJO {
    private final String key;
    private final String value;
}
