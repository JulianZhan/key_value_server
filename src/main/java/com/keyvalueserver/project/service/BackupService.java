package com.keyvalueserver.project.service;

import com.keyvalueserver.project.repository.KeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackupService extends BackupWorker {

    @Autowired
    public BackupService(KeyValueRepository keyValueRepository) {
        // call the constructor of the parent class, and inject the keyValueRepository
        super(keyValueRepository);
        // start the worker thread
        startWorkerThread();
    }

    private void startWorkerThread() {
        // init this class as a thread and start it
        Thread backupWorkerThread = new Thread(this);
        backupWorkerThread.start();
    }
}
