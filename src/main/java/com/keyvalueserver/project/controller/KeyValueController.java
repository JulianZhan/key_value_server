package com.keyvalueserver.project.controller;

import com.keyvalueserver.project.model.ApiResponse;
import com.keyvalueserver.project.service.KeyValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.keyvalueserver.project.model.KeyValuePOJO;
import com.keyvalueserver.project.model.KeyValuePair;

@RestController
@Slf4j
@RequestMapping("/keys")
public class KeyValueController {
    private final KeyValueService keyValueService;
    @Autowired
    public KeyValueController(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    @GetMapping("/{key}")
    public ResponseEntity<ApiResponse> getKeyValue(@PathVariable("key") String[] keys) {
        String[] value = keyValueService.getKeyValue(keys);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Key retrieved successfully", value));

    }

    @PostMapping
    public ResponseEntity<ApiResponse> postKeyValue(HttpServletRequest request) throws IOException {
        Gson gson = new Gson();
        // try with resources, automatically closes reader
        BufferedReader reader = request.getReader();
        KeyValuePOJO keyValuePOJO = gson.fromJson(reader, KeyValuePOJO.class);
        List<KeyValuePair> data = keyValuePOJO.getData();
        keyValueService.setKeyValue(data);
        reader.close();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Key added successfully", null));
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<ApiResponse> deleteKeyValue(@PathVariable("key") String[] keys) {
        keyValueService.deleteKeyValue(keys);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Key deleted successfully", null));
    }

    @GetMapping("/download")
    public void downloadKeyValuePairsAsCSV(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv; charset=UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"key_value_pairs.csv\"");
        keyValueService.exportCSVFile(servletResponse.getWriter());
    }
}
