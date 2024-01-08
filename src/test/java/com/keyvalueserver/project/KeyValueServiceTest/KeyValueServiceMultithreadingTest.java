package com.keyvalueserver.project.KeyValueServiceTest;

import com.keyvalueserver.project.exceptions.KeyNotFoundException;
import com.keyvalueserver.project.model.KeyValuePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import com.keyvalueserver.project.service.KeyValueService;
import com.keyvalueserver.project.model.KeyValuePOJO;


class KeyValueServiceMultithreadingTest {
    private KeyValueService keyValueService;
    private int numThreads;
    private int numIterations;

    @BeforeEach
    void setUp() {
        keyValueService = new KeyValueService();
        numThreads = 100;
        numIterations = 100000;
    }

    @Test
    void testConcurrentAccess() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numIterations; i++) {
            KeyValuePOJO keyValuePOJO = new KeyValuePOJO(List.of(new KeyValuePair("key" + i, "value" + i)));
            executor.submit(() -> {
                keyValueService.setKeyValue(keyValuePOJO.getData());
            });
        }

        for (int i = 0; i < numIterations; i++) {
            String[] keys = {"key" + i};
            int finalI = i;
            executor.submit(() -> {
                try {
                    String[] values = keyValueService.getKeyValue(keys);
                    assertEquals("value" + finalI, values[0]);
                } catch (KeyNotFoundException e) {
                    fail("Key not found: " + e.getMessage());
                }
            });
        }

        for (int i = 0; i < numIterations; i++) {
            String[] keys = {"key" + i};
            executor.submit(() -> {
                keyValueService.deleteKeyValue(keys);
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));

        for (int i = 0; i < numIterations; i++) {
            String[] keys = {"key" + i};
            assertThrows(KeyNotFoundException.class, () -> keyValueService.getKeyValue(keys));
        }
    }
}
