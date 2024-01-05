package com.keyvalueserver.project;

import com.keyvalueserver.project.Exceptions.KeyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.lang.Exception;

@RestController
public class KeyValueController {

    private final KeyValueService keyValueService;
    @Autowired
    public KeyValueController(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    @GetMapping("/keys/{key}")
    public ResponseEntity<ApiResponse> helloWorldGet(@PathVariable("key") String key) {
        try {
            String value = keyValueService.getKeyValue(key);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Key retrieved successfully", value));
        } catch (KeyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal server error", null));
        }

    }

    @PostMapping("/keys")
    public ResponseEntity<ApiResponse> helloWorldPost(@RequestBody KeyValuePOJO keyValuePOJO) {
        String key = keyValuePOJO.getKey();
        String value = keyValuePOJO.getValue();
        try {
            String response = keyValueService.setKeyValue(key, value);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, response, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal server error", null));
        }
    }

    @DeleteMapping("/keys/{key}")
    public ResponseEntity<ApiResponse> helloWorldDelete(@PathVariable("key") String key) {
        try {
            String response = keyValueService.deleteKeyValue(key);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, response, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal server error", null));
        }
    }
}
