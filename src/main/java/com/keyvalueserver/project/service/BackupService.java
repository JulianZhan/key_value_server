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
                    // if the data key contains true, it means that the data is to be inserted or updated
                    keyValueRepository.insertOrUpdateKeyValue(operation.getKeyValuePair());
                } else {
                    // if the data contains false, it means that the data is to be deleted
                    keyValueRepository.deleteKeyValue(operation.getKeyValuePair().getKey());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void addToBackupQueue(BackupOperation data) {
        backupQueue.add(data);
    }
}
