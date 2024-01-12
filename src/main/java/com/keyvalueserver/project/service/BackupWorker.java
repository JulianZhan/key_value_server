package com.keyvalueserver.project.service;

import com.keyvalueserver.project.model.BackupOperation;
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
                // take() method will block the thread if the queue is empty
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
        if (operation.isInsert()) {
            keyValueRepository.insertOrUpdateKeyValue(operation.getKeyValuePair());
        } else {
            // if the operation is delete, delete the key-value pair
            keyValueRepository.deleteKeyValue(operation.getKeyValuePair().getKey());
        }
    }

    public void addToBackupQueue(BackupOperation operation) {
        backupQueue.add(operation);
    }
}
