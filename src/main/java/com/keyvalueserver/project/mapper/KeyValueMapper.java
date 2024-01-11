package com.keyvalueserver.project.mapper;

import com.keyvalueserver.project.model.KeyValuePair;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KeyValueMapper {
    void insertOrUpdateKeyValue(KeyValuePair keyValue);
    void deleteKeyValue(String key);
}
