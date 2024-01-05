package com.keyvalueserver.project.controller;

import com.keyvalueserver.project.exceptions.KeyNotFoundException;
import com.keyvalueserver.project.service.KeyValueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.lang.Exception;

import javax.servlet.http.HttpServletRequest;

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

        String key = keyValuePOJO.getKey();
        String value = keyValuePOJO.getValue();
        try {
            String response = keyValueService.setKeyValue(key, value);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, response, null));
        } catch (IllegalArgumentException e) {
            log.error(String.format("Key or value may be null %s %s. Method: Post, Path: /keys", key, value));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error(String.format("Internal server error %s %s. Method: Post, Path: /keys", key, value));
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
