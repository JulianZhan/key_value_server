package com.keyvalueserver.project.backup_support;

import com.keyvalueserver.project.keyvalue.KeyValuePair;
import com.keyvalueserver.project.keyvalue.KeyValueRepository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class BackupWorker implements Runnable {
    protected final BlockingQueue<BackupQueueEntry> backupQueue = new LinkedBlockingQueue<>();
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
                BackupQueueEntry entry = backupQueue.take();
                processEntryQueue(entry);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    protected void processEntryQueue(BackupQueueEntry entry) {
        BackupOperation operation = entry.getBackupOperation();
        CompletableFuture<Void> completableFuture = entry.getCompletableFuture();
        OperationType operationType = operation.getOperationType();
        KeyValuePair keyValuePair = operation.getKeyValuePair();
        // if the operation is insert, insert or update the key-value pair
        if (operationType.equals(OperationType.INSERT)) {
            keyValueRepository.insertOrUpdateKeyValue(keyValuePair);
        } else if (operationType.equals(OperationType.DELETE)) {
            // if the operation is delete, delete the key-value pair
            String key = keyValuePair.getKey();
            keyValueRepository.deleteKeyValue(key);
        }
        completableFuture.complete(null);
    }

    public CompletableFuture<Void> addToBackupQueue(BackupOperation operation) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        BackupQueueEntry entry = new BackupQueueEntry(operation, future);
        backupQueue.add(entry);
        return future;
    }
}
