package com.keyvalueserver.project.service;

import com.keyvalueserver.project.repository.KeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BackupRetrievalService {
    private final KeyValueRepository keyValueRepository;

    @Autowired
    public BackupRetrievalService(KeyValueRepository keyValueRepository) {
        this.keyValueRepository = keyValueRepository;
    }

    public Optional<String> getKeyValue(String key) {
        return Optional.ofNullable(keyValueRepository.getKeyValue(key));
    }
}
