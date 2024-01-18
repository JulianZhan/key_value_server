package com.keyvalueserver.project.backup_support;

import lombok.Getter;
@Getter
public class BackupQueueEntry {
    private final BackupOperation backupOperation;
    private boolean isCompleted = false;

    public BackupQueueEntry(BackupOperation backupOperation) {
        this.backupOperation = backupOperation;
    }

    // methods to set and check completion
    public synchronized void complete() {
        this.isCompleted = true;
        notifyAll();
    }

    public synchronized void awaitCompletion() throws InterruptedException {
        while (!isCompleted) {
            wait();
        }
    }
}
