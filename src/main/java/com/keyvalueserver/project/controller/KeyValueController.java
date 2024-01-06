package com.keyvalueserver.project.controller;

import com.keyvalueserver.project.exceptions.KeyNotFoundException;
import com.keyvalueserver.project.model.ApiResponse;
import com.keyvalueserver.project.service.KeyValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.lang.Exception;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import com.keyvalueserver.project.model.KeyValuePOJO;
import com.keyvalueserver.project.model.KeyValuePair;

@RestController
@Slf4j
public class KeyValueController {
    private final KeyValueService keyValueService;
    @Autowired
    public KeyValueController(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    @GetMapping("/keys/{key}")
    public ResponseEntity<ApiResponse> getKeyValue(@PathVariable("key") String[] keys) {
        String[] value = keyValueService.getKeyValue(keys);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Key retrieved successfully", value));

    }

    @PostMapping("/keys")
    public ResponseEntity<ApiResponse> postKeyValue(HttpServletRequest request) {
        Gson gson = new Gson();
        // try with resources, automatically closes reader
        try (BufferedReader reader = request.getReader()) {
            KeyValuePOJO keyValuePOJO = gson.fromJson(reader, KeyValuePOJO.class);
            List<KeyValuePair> data = keyValuePOJO.getData();
            keyValueService.setKeyValue(data);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Key added successfully", null));
        } catch (IOException e) {
            log.error(String.format("Error reading request body %s. Method: Post, Path: /keys", e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Error reading request body", null));
        }
    }

    @DeleteMapping("/keys/{key}")
    public ResponseEntity<ApiResponse> deleteKeyValue(@PathVariable("key") String[] keys) {
        keyValueService.deleteKeyValue(keys);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Key deleted successfully", null));
    }
}
