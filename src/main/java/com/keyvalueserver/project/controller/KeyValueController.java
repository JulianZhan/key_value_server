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
        // use try with resources, automatically closes reader
        try (BufferedReader reader = request.getReader()) {
            // convert JSON to predefined POJO
            KeyValuePOJO keyValuePOJO = gson.fromJson(reader, KeyValuePOJO.class);
            // use getter to access data
            List<KeyValuePair> data = keyValuePOJO.getData();
            keyValueService.setKeyValue(data);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Key added successfully", null));
        }
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<ApiResponse> deleteKeyValue(@PathVariable("key") String[] keys) {
        keyValueService.deleteKeyValue(keys);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Key deleted successfully", null));
    }

    @GetMapping("/download")
    public void downloadKeyValuePairsAsCSV(HttpServletResponse servletResponse) throws IOException {
        // UTF-8 encoding is required for non-English characters
        servletResponse.setContentType("text/csv; charset=UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        // Content-Disposition header is used to specify the name of the file
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"key_value_pairs.csv\"");
        // send printWriter to service layer to write CSV file, which will be sent to client
        keyValueService.exportCSVFile(servletResponse.getWriter());
    }
}
