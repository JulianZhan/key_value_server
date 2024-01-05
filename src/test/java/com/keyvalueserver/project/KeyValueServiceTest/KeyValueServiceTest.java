package com.keyvalueserver.project.KeyValueServiceTest;

import com.keyvalueserver.project.service.KeyValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeyValueServiceTest {

    private KeyValueService keyValueService;

    @BeforeEach
    void setUp() {
        keyValueService = new KeyValueService();
    }

    @Test
    void setKeyValueWithValidInputs() {
        KeyValueService keyValueService = new KeyValueService();
        String result = keyValueService.setKeyValue("key", "value");
        assertEquals("Key value pair added", result);
    }

    @Test
    void getKeyValueWithValidKey() {
        KeyValueService keyValueService = new KeyValueService();
        keyValueService.setKeyValue("key", "value");
        String result = keyValueService.getKeyValue("key");
        assertEquals("value", result);
    }

    @Test
    void deleteKeyValueWithValidKey() {
        KeyValueService keyValueService = new KeyValueService();
        keyValueService.setKeyValue("key", "value");
        String result = keyValueService.deleteKeyValue("key");
        assertEquals("Key value pair deleted", result);
    }

    @Test
    void setKeyValueWithNullKey() {
        KeyValueService keyValueService = new KeyValueService();
        assertThrows(IllegalArgumentException.class, () -> {
            keyValueService.setKeyValue(null, "value");
        });
    }

    @Test
    void setKeyValueWithNullValue() {
        KeyValueService keyValueService = new KeyValueService();
        assertThrows(IllegalArgumentException.class, () -> {
            keyValueService.setKeyValue("key", null);
        });
    }

    @Test
    void getKeyValueWithNullKey() {
        KeyValueService keyValueService = new KeyValueService();
        assertThrows(IllegalArgumentException.class, () -> {
            keyValueService.getKeyValue(null);
        });
    }
}
