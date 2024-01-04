package com.keyvalueserver.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class KeyValueServiceTest {

    private KeyValueService keyValueService;

    @BeforeEach
    void setUp() {
        keyValueService = new KeyValueService();
    }

    @Test
    void testGetKeyValue() {
        keyValueService.setKeyValue("testKey", "testValue");
        String value = keyValueService.getKeyValue("testKey");
        assertEquals("testValue", value);
        keyValueService.setKeyValue("testKey2", "testValue2");
    }

    @Test
    void testSetKeyValue() {
        String response = keyValueService.setKeyValue("testKey", "testValue");
        assertEquals("Key value pair added", response);
        assertEquals("testValue", keyValueService.getKeyValue("testKey"));
    }

    @Test
    void testDeleteKeyValue() {
        keyValueService.setKeyValue("testKey", "testValue");
        String response = keyValueService.deleteKeyValue("testKey");
        assertEquals("Key value pair deleted", response);
        assertNull(keyValueService.getKeyValue("testKey"));
    }
}
