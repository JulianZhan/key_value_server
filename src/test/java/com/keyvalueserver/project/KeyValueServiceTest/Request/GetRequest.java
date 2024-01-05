package com.keyvalueserver.project.KeyValueServiceTest.Request;

import com.keyvalueserver.project.KeyValueService;
import org.springframework.beans.factory.annotation.Autowired;

public class GetRequest implements Runnable {
    private final KeyValueService keyValueService;
    private final String key;
    private String value;

    @Autowired
    public GetRequest(KeyValueService keyValueService, String key) {
        this.keyValueService = keyValueService;
        this.key = key;
    }

    public void run() {
        this.value = keyValueService.getKeyValue(key);
    }

    public String getValue() {
        return value;
    }
}
