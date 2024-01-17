package com.keyvalueserver.project.service;

import com.keyvalueserver.project.model.KeyValuePair;
import com.keyvalueserver.project.repository.KeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataOperationService {
    private final KeyValueRepository keyValueRepository;

    @Autowired
    public DataOperationService(KeyValueRepository keyValueRepository) {
        this.keyValueRepository = keyValueRepository;
    }

    public void insertOrUpdateKeyValue(KeyValuePair keyValuePair) {
        keyValueRepository.insertOrUpdateKeyValue(keyValuePair);
    }

    public String getKeyValue(String key) {
        return keyValueRepository.getKeyValue(key);
    }

    public void deleteKeyValue(String key) {
        keyValueRepository.deleteKeyValue(key);
    }

}
