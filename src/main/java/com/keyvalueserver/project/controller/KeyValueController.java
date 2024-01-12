package com.keyvalueserver.project.controller;

import com.keyvalueserver.project.exceptions.ErrorMessage;
import com.keyvalueserver.project.model.KeyValueApiResponse;
import com.keyvalueserver.project.service.KeyValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Get value of key",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Auth", required = true,
                            description = "JWT",
                            schema = @Schema(implementation = String.class)),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(description = "Success",
                                                    implementation = KeyValueApiResponse.class)
                                    )
                            }),
            }
    )
    @GetMapping("/{key}")
    public ResponseEntity<KeyValueApiResponse> getKeyValue(@PathVariable("key") String[] keys) {
        String[] value = keyValueService.getKeyValue(keys);
        return ResponseEntity.status(HttpStatus.OK).body(new KeyValueApiResponse(true, "Key retrieved successfully", value));
    }

    @Operation(summary = "Add key-value pair",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Auth", required = true,
                            description = "JWT",
                            schema = @Schema(implementation = String.class)),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ClientRequest body.",
                    content = @Content(schema = @Schema(implementation = KeyValuePOJO.class)),
                    required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(description = "Success",
                                                    implementation = KeyValueApiResponse.class)
                                    )
                            }),
            }
    )
    @PostMapping
    public ResponseEntity<KeyValueApiResponse> postKeyValue(HttpServletRequest request) throws IOException {
        Gson gson = new Gson();
        // use try with resources, automatically closes reader
        try (BufferedReader reader = request.getReader()) {
            // convert JSON to predefined POJO
            KeyValuePOJO keyValuePOJO = gson.fromJson(reader, KeyValuePOJO.class);
            if (keyValuePOJO == null || keyValuePOJO.getData() == null) {
                throw new IllegalArgumentException(ErrorMessage.INVALID_REQUEST);
            }
            // use getter to access data
            List<KeyValuePair> data = keyValuePOJO.getData();
            keyValueService.setKeyValue(data);
            return ResponseEntity.status(HttpStatus.OK).body(new KeyValueApiResponse(true, "Key added successfully", null));
        }
    }

    @Operation(summary = "Delete key-value pair",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Auth", required = true,
                            description = "JWT",
                            schema = @Schema(implementation = String.class)),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(description = "Success",
                                                    implementation = KeyValueApiResponse.class)
                                    )
                            }),
            }
    )
    @DeleteMapping("/{key}")
    public ResponseEntity<KeyValueApiResponse> deleteKeyValue(@PathVariable("key") String[] keys) {
        keyValueService.deleteKeyValue(keys);
        return ResponseEntity.status(HttpStatus.OK).body(new KeyValueApiResponse(true, "Key deleted successfully", null));
    }


    @Operation(summary = "Download key-value pairs as CSV file",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Auth", required = true,
                            description = "JWT",
                            schema = @Schema(implementation = String.class)),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {
                                    @Content(
                                            mediaType = "text/csv",
                                            schema = @Schema(description = "Success")
                                    )
                            }),
            }
    )
    @GetMapping("/download")
    public void downloadKeyValuePairsAsCSV(HttpServletResponse servletResponse) throws IOException {
        // UTF-8 encoding is required for non-English characters
        servletResponse.setContentType("text/csv; charset=UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        // Content-Disposition header is used to specify the name of the file
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"key_value_pairs.csv\"");
        // send printWriter to service layer to write CSV file, which will be sent to client
        keyValueService.exportCSVFile(servletResponse.getWriter());
    }
}
