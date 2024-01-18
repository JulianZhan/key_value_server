package com.keyvalueserver.project.KeyValueService;

import com.keyvalueserver.project.backup_support.BackupOperation;
import com.keyvalueserver.project.backup_support.BackupOperationFactory;
import com.keyvalueserver.project.backup_support.OperationType;
import com.keyvalueserver.project.backup_support.BackupRetrievalService;
import com.keyvalueserver.project.backup_support.BackupService;
import com.keyvalueserver.project.keyvalue.KeyValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.keyvalueserver.project.keyvalue.KeyValuePair;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


public class KeyValueServiceCSVTest {

    private KeyValueService keyValueService;
    @Mock
    private BackupService backupService;
    @Mock
    private BackupOperationFactory SimpleBackupOperationFactory;
    @Mock
    private BackupRetrievalService backupRetrievalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
//        when(backupService.addToBackupQueue(any(BackupOperation.class))).thenReturn(CompletableFuture.completedFuture(null)
        // Create a mock BackupOperation
        BackupOperation mockBackupOperation = new BackupOperation(new KeyValuePair("test", "test"));
        mockBackupOperation.setOperationType(OperationType.INSERT);

        when(SimpleBackupOperationFactory.createBackupOperation(any(KeyValuePair.class), any(OperationType.class)))
                .thenReturn(mockBackupOperation);

        keyValueService = new KeyValueService(backupService, SimpleBackupOperationFactory, backupRetrievalService);
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