//package com.keyvalueserver.project.KeyValueServiceTest.request;
//
//import com.keyvalueserver.project.service.KeyValueService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class DeleteRequest implements Runnable {
//    private final KeyValueService keyValueService;
//    private final String key;
//
//    @Autowired
//    public DeleteRequest(KeyValueService keyValueService, String key) {
//        this.keyValueService = keyValueService;
//        this.key = key;
//    }
//
//    public void run() {
//        keyValueService.deleteKeyValue(key);
//    }
//}
