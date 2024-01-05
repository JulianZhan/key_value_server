package com.keyvalueserver.project.KeyValueServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.keyvalueserver.project.service.KeyValueService;
import com.keyvalueserver.project.KeyValueServiceTest.request.SetRequest;
import com.keyvalueserver.project.KeyValueServiceTest.request.GetRequest;
import com.keyvalueserver.project.KeyValueServiceTest.request.DeleteRequest;


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
