package com.keyvalueserver.project.service;

import com.keyvalueserver.project.model.BackupOperation;
import com.keyvalueserver.project.model.KeyValuePair;
import com.keyvalueserver.project.model.OperationType;
import com.keyvalueserver.project.repository.KeyValueRepository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BackupWorker implements Runnable {
    protected final BlockingQueue<BackupOperation> backupQueue = new LinkedBlockingQueue<>();
    protected final DataOperationService dataOperationService;

    public BackupWorker(DataOperationService dataOperationService) {
        this.dataOperationService = dataOperationService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                /*
                take() method will block the thread if the queue is empty
                when thread is being blocked, it will not consume CPU resources
                and thread is in WAITING state
                once there is an element in the queue, thread will be notified
                and it will be in RUNNABLE state
                 */
                BackupOperation operation = backupQueue.take();
                processOperation(operation);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    protected void processOperation(BackupOperation operation) {
        OperationType operationType = operation.getOperationType();
        KeyValuePair keyValuePair = operation.getKeyValuePair();
        // if the operation is insert, insert or update the key-value pair
        if (operationType.equals(OperationType.INSERT)) {
            dataOperationService.insertOrUpdateKeyValue(keyValuePair);
        } else if (operationType.equals(OperationType.DELETE)) {
            // if the operation is delete, delete the key-value pair
            String key = keyValuePair.getKey();
            dataOperationService.deleteKeyValue(key);
        }
    }

    public void addToBackupQueue(BackupOperation operation) {
        backupQueue.add(operation);
    }
}
