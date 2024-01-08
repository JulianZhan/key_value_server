package com.keyvalueserver.project.KeyValueServiceTest;

import com.keyvalueserver.project.service.KeyValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import com.keyvalueserver.project.model.KeyValuePair;
import java.io.IOException;
import java.util.Arrays;
import java.io.StringWriter;
import java.io.PrintWriter;





public class KeyValueServiceCSVTest {

    private KeyValueService keyValueService;

    @BeforeEach
    void setUp() {
        keyValueService = new KeyValueService();
    }

    @Test
    void testExportCSVFile() throws IOException {
        // Create an instance of KeyValueService
        KeyValueService keyValueService = new KeyValueService();

        // Add some test data
        keyValueService.setKeyValue(Arrays.asList(
                new KeyValuePair("key1", "value1"),
                new KeyValuePair("key2", "嗨嗨"),
                new KeyValuePair("key3", "哈哈")
        ));

        // Create a StringWriter to capture the output
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        // Call the method under test
        keyValueService.exportCSVFile(printWriter);

        // Verify the output
        String expectedOutput = "Key,Value\r\nkey1,value1\r\nkey2,嗨嗨\r\nkey3,哈哈\r\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }
}