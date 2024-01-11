package com.keyvalueserver.project.mapper;

import com.keyvalueserver.project.model.KeyValuePair;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KeyValueMapper {
    void insertOrUpdateKeyValue(KeyValuePair keyValue);
    void deleteKeyValue(String key);
}
