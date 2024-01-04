package com.keyvalueserver.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SetRequest implements Runnable {
    private KeyValueService keyValueService;
    private String key;
    private String value;

    protected SetRequest(KeyValueService keyValueService, String key, String value) {
        this.keyValueService = keyValueService;
        this.key = key;
        this.value = value;
    }

    public void run() {
        keyValueService.setKeyValue(key, value);
    }
}

class GetRequest implements Runnable {
    private KeyValueService keyValueService;
    private String key;
    private String value;

    protected GetRequest(KeyValueService keyValueService, String key) {
        this.keyValueService = keyValueService;
        this.key = key;
    }

    public void run() {
        value = keyValueService.getKeyValue(key);
    }

    public String getValue() {
        return value;
    }
}

class DeleteRequest implements Runnable {
    private KeyValueService keyValueService;
    private String key;

    protected DeleteRequest(KeyValueService keyValueService, String key) {
        this.keyValueService = keyValueService;
        this.key = key;
    }

    public void run() {
        keyValueService.deleteKeyValue(key);
    }
}



public class KeyValueServiceMultithreadingTest {

    private KeyValueService keyValueService;

    @BeforeEach
    void setUp() {
        keyValueService = new KeyValueService();
    }

    @Test
    void testKeyValueMultithreading() {
        final int numThreads = 10000;
        Thread[] threads = new Thread[numThreads];
        GetRequest[] getRequest = new GetRequest[numThreads];

        for (int i = 0; i < numThreads; i++) {
            SetRequest setRequest = new SetRequest(keyValueService, "testKey" + i, "testValue" + i);
            threads[i] = new Thread(setRequest);
            threads[i].start();
        }
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
        for (int i = 0; i < numThreads; i++) {
            getRequest[i] = new GetRequest(keyValueService, "testKey" + i);
            threads[i] = new Thread(getRequest[i]);
            threads[i].start();
        }
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
                assertEquals("testValue" + i, getRequest[i].getValue());
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
        for (int i = 0; i < numThreads; i++) {
            DeleteRequest deleteRequest = new DeleteRequest(keyValueService, "testKey" + i);
            threads[i] = new Thread(deleteRequest);
            threads[i].start();
        }
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
        for (int i = 0; i < numThreads; i++) {
            getRequest[i] = new GetRequest(keyValueService, "testKey" + i);
            threads[i] = new Thread(getRequest[i]);
            threads[i].start();
        }
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
                assertNull(getRequest[i].getValue());
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }

}

