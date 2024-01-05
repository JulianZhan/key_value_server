package com.keyvalueserver.project.KeyValueServiceTest.Request;

import com.keyvalueserver.project.KeyValueService;

public class SetRequest implements Runnable {
    private final KeyValueService keyValueService;
    private final String key;
    private final String value;

    public SetRequest(KeyValueService keyValueService, String key, String value) {
        this.keyValueService = keyValueService;
        this.key = key;
        this.value = value;
    }

    public void run() {
        keyValueService.setKeyValue(key, value);
    }
}
