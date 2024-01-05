package com.keyvalueserver.project.controller;

import com.keyvalueserver.project.exceptions.KeyNotFoundException;
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
    public ResponseEntity<ApiResponse> getKeyValue(@PathVariable("key") String key) {
        try {
            String value = keyValueService.getKeyValue(key);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Key retrieved successfully", value));
        } catch (KeyNotFoundException e) {
            log.error(String.format("Key %s not found. Method: Get, Path: /keys/{%s}", key, key));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            log.error(String.format("Key may be null %s. Method: Get, Path: /keys/{%s}", key, key));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error(String.format("Internal server error %s. Method: Get, Path: /keys/{%s}", key, key));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal server error", null));
        }

    }

    @PostMapping("/keys")
    public ResponseEntity<ApiResponse> postKeyValue(HttpServletRequest request) {
        Gson gson = new Gson();
        try (BufferedReader reader = request.getReader()) {
            KeyValuePOJO keyValuePOJO = gson.fromJson(reader, KeyValuePOJO.class);
            List<KeyValuePair> data = keyValuePOJO.getData();
            String response = keyValueService.setKeyValue(data);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, response, null));
        } catch (IOException e) {
            log.error(String.format("%s. Error reading request body. Method: Post, Path: /keys", e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Error reading request body", null));
        } catch (IllegalArgumentException e) {
            log.error(String.format("%s. Key or value may be null. Method: Post, Path: /keys", e));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Key or value cannot be null", null));
        } catch (Exception e) {
            log.error(String.format("%s. Internal server error. Method: Post, Path: /keys", e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal server error", null));
        }
    }

    @DeleteMapping("/keys/{key}")
    public ResponseEntity<ApiResponse> deleteKeyValue(@PathVariable("key") String key) {
        try {
            String response = keyValueService.deleteKeyValue(key);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, response, null));
        } catch (IllegalArgumentException e) {
            log.error(String.format("Key may be null %s. Method: Delete, Path: /keys/{%s}", key, key));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error(String.format("Internal server error %s. Method: Delete, Path: /keys/{%s}", key, key));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal server error", null));
        }
    }
}
