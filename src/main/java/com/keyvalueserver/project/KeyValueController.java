package com.keyvalueserver.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class KeyValueController {

    private final KeyValueService keyValueService;
    @Autowired
    public KeyValueController(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    @GetMapping("/keys/{key}")
    public String helloWorldGet(@PathVariable("key") String key) {
        return this.keyValueService.getKeyValue(key);
    }

    @PostMapping("/keys")
    public String helloWorldPost(@RequestBody KeyValuePOJO keyValuePOJO) {
        String key = keyValuePOJO.getKey();
        String value = keyValuePOJO.getValue();
        return this.keyValueService.setKeyValue(key, value);
    }

    @DeleteMapping("/keys/{key}")
    public String helloWorldDelete(@PathVariable("key") String key) {
        return this.keyValueService.deleteKeyValue(key);
    }
}
