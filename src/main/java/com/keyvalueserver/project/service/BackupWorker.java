package com.keyvalueserver.project.service;

import com.keyvalueserver.project.model.BackupOperation;
import com.keyvalueserver.project.model.OperationType;
import com.keyvalueserver.project.repository.KeyValueRepository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BackupWorker implements Runnable {
    protected final BlockingQueue<BackupOperation> backupQueue = new LinkedBlockingQueue<>();
    protected final KeyValueRepository keyValueRepository;

    public BackupWorker(KeyValueRepository keyValueRepository) {
        this.keyValueRepository = keyValueRepository;
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
        // if the operation is insert, insert or update the key-value pair
        if (operation.getOperationType().equals(OperationType.INSERT)) {
            keyValueRepository.insertOrUpdateKeyValue(operation.getKeyValuePair());
        } else if (operation.getOperationType().equals(OperationType.DELETE)) {
            // if the operation is delete, delete the key-value pair
            keyValueRepository.deleteKeyValue(operation.getKeyValuePair().getKey());
        }
    }

    public void addToBackupQueue(BackupOperation operation) {
        backupQueue.add(operation);
    }
}
