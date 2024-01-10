package com.keyvalueserver.project.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.IOException;
import com.keyvalueserver.project.model.KeyValueApiResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KeyNotFoundException.class)
    public ResponseEntity<KeyValueApiResponse> handleKeyNotFoundException(KeyNotFoundException ex, HttpServletRequest request) {
        KeyValueApiResponse keyValueApiResponse = new KeyValueApiResponse(false, "Key not found", null);
        logErrorWithRequestInfo(ex, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(keyValueApiResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<KeyValueApiResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        KeyValueApiResponse errorResponse = new KeyValueApiResponse(false, "key or value cannot be null", null);
        logErrorWithRequestInfo(ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<KeyValueApiResponse> handleIOException(IOException ex, HttpServletRequest request) {
        KeyValueApiResponse errorResponse = new KeyValueApiResponse(false, "Error reading request body", null);
        logErrorWithRequestInfo(ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<KeyValueApiResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        KeyValueApiResponse errorResponse = new KeyValueApiResponse(false, "Internal server error", null);
        logErrorWithRequestInfo(ex, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private void logErrorWithRequestInfo(Exception ex, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        log.error("Exception occurred at URL: " + requestUrl, ex);
    }
}
