package com.keyvalueserver.project.KeyValueServiceTest;

import com.keyvalueserver.project.service.KeyValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.util.Arrays;
import java.util.List;
import com.keyvalueserver.project.model.KeyValuePair;
import com.keyvalueserver.project.exceptions.KeyNotFoundException;



public class KeyValueServiceTest {

    private KeyValueService keyValueService;

    @BeforeEach
    void setUp() {
        keyValueService = new KeyValueService();
    }

    @Test
    void testSetAndGetKeyValue() {
        List<KeyValuePair> keyValuePairs = Arrays.asList(
                new KeyValuePair("key1", "value1"),
                new KeyValuePair("key2", "value2")
        );
        keyValueService.setKeyValue(keyValuePairs);

        String[] keys = {"key1", "key2"};
        String[] values = keyValueService.getKeyValue(keys);

        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test
    void testKeyNotFound() {
        String[] keys = {"nonExistingKey"};
        KeyNotFoundException exception = assertThrows(
                KeyNotFoundException.class,
                () -> keyValueService.getKeyValue(keys)
        );

        assertEquals("Key nonExistingKey not found", exception.getMessage());
    }

    @Test
    void testNullKeyInSet() {
        List<KeyValuePair> keyValuePairs = Arrays.asList(
                new KeyValuePair(null, "value1")
        );

        assertThrows(IllegalArgumentException.class, () -> keyValueService.setKeyValue(keyValuePairs));
    }

    @Test
    void testNullValueInSet() {
        List<KeyValuePair> keyValuePairs = Arrays.asList(
                new KeyValuePair("key1", null)
        );

        assertThrows(IllegalArgumentException.class, () -> keyValueService.setKeyValue(keyValuePairs));
    }

    @Test
    void testNullKeyInGet() {
        String[] keys = {null};
        assertThrows(IllegalArgumentException.class, () -> keyValueService.getKeyValue(keys));
    }

    @Test
    void testDeleteKeyValue() {
        // Setting a key-value pair
        List<KeyValuePair> keyValuePairs = Arrays.asList(new KeyValuePair("key1", "value1"));
        keyValueService.setKeyValue(keyValuePairs);

        // Deleting the key-value pair
        String[] keysToDelete = {"key1"};
        keyValueService.deleteKeyValue(keysToDelete);

        // Trying to retrieve deleted key
        String[] keys = {"key1"};
        assertThrows(KeyNotFoundException.class, () -> keyValueService.getKeyValue(keys));
    }

    @Test
    void testNullKeyInDelete() {
        String[] keys = {null};
        assertThrows(IllegalArgumentException.class, () -> keyValueService.deleteKeyValue(keys));
    }

}
