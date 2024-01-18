package com.keyvalueserver.project.backup_support;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
public class BackupQueueEntry {
    private final BackupOperation backupOperation;
    private final CompletableFuture<Void> completableFuture;

    public BackupQueueEntry(BackupOperation backupOperation, CompletableFuture<Void> completableFuture) {
        this.backupOperation = backupOperation;
        this.completableFuture = completableFuture;
    }
}
