package com.keyvalueserver.project.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class KeyValuePOJO {
    private final List<KeyValuePair> data;
}
