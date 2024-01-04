package com.keyvalueserver.project;

public class KeyValuePOJO {
    private final String key;
    private final String value;

    public KeyValuePOJO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

}
