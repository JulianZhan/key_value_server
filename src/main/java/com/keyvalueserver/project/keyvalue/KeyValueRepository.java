package com.keyvalueserver.project.keyvalue;

import com.keyvalueserver.project.mapper.KeyValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class KeyValueRepository {
    private final KeyValueMapper keyValueMapper;

    @Autowired
    public KeyValueRepository(KeyValueMapper keyValueMapper) {
        this.keyValueMapper = keyValueMapper;
    }

    public void insertOrUpdateKeyValue(KeyValuePair keyValue) {
        keyValueMapper.insertOrUpdateKeyValue(keyValue);
    }

    public String getKeyValue(String key) {
        return keyValueMapper.getKeyValue(key);
    }

    public void deleteKeyValue(String key) {
        keyValueMapper.deleteKeyValue(key);
    }
}
