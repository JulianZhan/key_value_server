package com.keyvalueserver.project.mapper;

import com.keyvalueserver.project.keyvalue.KeyValuePair;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KeyValueMapper {
    void insertOrUpdateKeyValue(KeyValuePair keyValue);
    String getKeyValue(String keys);
    void deleteKeyValue(String key);
}
