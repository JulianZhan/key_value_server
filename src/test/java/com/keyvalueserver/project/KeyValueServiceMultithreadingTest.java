package com.keyvalueserver.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;



class RunnableRequest implements Runnable {

    private KeyValueService keyValueService;
    private String operation;
    private String key;
    private String value;

    protected RunnableRequest(KeyValueService keyValueService, String operation, String key, String value) {
        this.keyValueService = keyValueService;
        this.operation = operation;
        this.key = key;
        this.value = value;
    }

    public void run() {
        if (operation.equals("set")) {
            keyValueService.setKeyValue(key, value);
        } else if (operation.equals("get")) {
            keyValueService.getKeyValue(key);
        } else if (operation.equals("delete")) {
            keyValueService.deleteKeyValue(key);
        }
    }
}

public class KeyValueServiceMultithreadingTest {

    private KeyValueService keyValueService;
    private final int numThreads = 10000;

    @BeforeEach
    void setUp() {
        keyValueService = new KeyValueService();
    }

    @Test
    void testKeyValueMultithreading() {
        for (int i = 0; i < numThreads; i++) {
            RunnableRequest runnableRequest = new RunnableRequest(keyValueService, "set", "testKey" + i, "testValue" + i);
            Thread thread = new Thread(runnableRequest);
            thread.start();
        }
        for (int i = 0; i < numThreads; i++) {
            String value = keyValueService.getKeyValue("testKey" + i);
            assertEquals(value, "testValue" + i);
        }
        for (int i = 0; i < numThreads; i++) {
            RunnableRequest runnableRequest = new RunnableRequest(keyValueService, "delete", "testKey" + i, "testValue" + i);
            Thread thread = new Thread(runnableRequest);
            thread.start();
        }
        for (int i = 0; i < numThreads; i++) {
            String value = keyValueService.getKeyValue("testKey" + i);
            assertNull(value);
        }
    }

}

