package com.keyvalueserver.project.KeyValueService;

import com.keyvalueserver.project.repository.KeyValueRepository;
import com.keyvalueserver.project.service.BackupService;
import com.keyvalueserver.project.service.KeyValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.keyvalueserver.project.model.KeyValuePair;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.io.StringWriter;
import java.io.PrintWriter;
import static org.junit.jupiter.api.Assertions.fail;





public class KeyValueServiceCSVTest {

    private KeyValueService keyValueService;
    @Mock
    private BackupService backupService;
    @Mock
    private KeyValueRepository keyValueRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        keyValueService = new KeyValueService(backupService, keyValueRepository);
    }

    @Test
    public void testWriteCSVHeader() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        try {
            keyValueService.exportCSVFile(printWriter);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        String expectedOutput = "Key,Value\r\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testWriteKeyValuePairs() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        keyValueService.setKeyValue(Arrays.asList(
                new KeyValuePair("key1", "嗨嗨"),
                new KeyValuePair("測測", "value2")
        ));

        try {
            keyValueService.exportCSVFile(printWriter);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        String expectedOutput = "Key,Value\r\nkey1,嗨嗨\r\n測測,value2\r\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }
}