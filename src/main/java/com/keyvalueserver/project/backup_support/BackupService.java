package com.keyvalueserver.project.backup_support;

import com.keyvalueserver.project.keyvalue.KeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackupService extends BackupWorker {

    @Autowired
    public BackupService(KeyValueRepository keyValueRepository) {
        /*
        call the constructor of the parent class, and inject the keyValueRepository
        this will allow the parent class to use the keyValueRepository
        and the child class to use full functionality of the parent class directly
         */
        super(keyValueRepository);
        // start the worker thread
        startWorkerThread();
    }

    private void startWorkerThread() {
        // run the current class as a thread
        Thread backupWorkerThread = new Thread(this);
        backupWorkerThread.start();
    }
}
