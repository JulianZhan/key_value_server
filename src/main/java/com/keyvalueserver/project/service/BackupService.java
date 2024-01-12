package com.keyvalueserver.project.service;

import com.keyvalueserver.project.model.BackupOperation;
import com.keyvalueserver.project.repository.KeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

@Service
public class BackupService extends Thread {

    private final BlockingQueue<BackupOperation> backupQueue = new LinkedBlockingQueue<>();
    private final KeyValueRepository keyValueRepository;

    @Autowired
    public BackupService(KeyValueRepository keyValueRepository) {
        this.keyValueRepository = keyValueRepository;
        // daemon thread will not prevent the application from shutting down
        setDaemon(true);
    }

    // use @PostConstruct to start the thread as soon as the application starts
    @PostConstruct
    public void startThread() {
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                // take() will block the thread until there is data in the queue
                BackupOperation operation = backupQueue.take();
                if (operation.isInsert()) {
                    // if operation is insert, insert or update the key value pair in the database
                    keyValueRepository.insertOrUpdateKeyValue(operation.getKeyValuePair());
                } else {
                    // if operation is delete, delete the key value pair from the database
                    keyValueRepository.deleteKeyValue(operation.getKeyValuePair().getKey());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // add backup operation to the queue
    public void addToBackupQueue(BackupOperation operation) {
        backupQueue.add(operation);
    }
}
