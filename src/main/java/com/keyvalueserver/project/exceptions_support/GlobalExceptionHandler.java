package com.keyvalueserver.project.exceptions_support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.IOException;
import com.keyvalueserver.project.keyvalue.KeyValueApiResponse;
import javax.servlet.http.HttpServletRequest;
import com.google.gson.JsonSyntaxException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KeyNotFoundException.class)
    public ResponseEntity<KeyValueApiResponse> handleKeyNotFoundException(KeyNotFoundException ex, HttpServletRequest request) {
        KeyValueApiResponse errorResponse = new KeyValueApiResponse(false, ex.getMessage(), null);
        logErrorWithRequestInfo(ex, request, errorResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<KeyValueApiResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        KeyValueApiResponse errorResponse = new KeyValueApiResponse(false, ex.getMessage(), null);
        logErrorWithRequestInfo(ex, request, errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<KeyValueApiResponse> handleIOException(IOException ex, HttpServletRequest request) {
        KeyValueApiResponse errorResponse = new KeyValueApiResponse(false, ErrorMessage.IO_EXCEPTION, null);
        logErrorWithRequestInfo(ex, request, errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(JsonSyntaxException.class)
    public ResponseEntity<KeyValueApiResponse> handleJsonSyntaxException(JsonSyntaxException ex, HttpServletRequest request) {
        KeyValueApiResponse errorResponse = new KeyValueApiResponse(false, ErrorMessage.INVALID_REQUEST, null);
        logErrorWithRequestInfo(ex, request, errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<KeyValueApiResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        KeyValueApiResponse errorResponse = new KeyValueApiResponse(false, ErrorMessage.INTERNAL_SERVER_ERROR, null);
        logErrorWithRequestInfo(ex, request, errorResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private void logErrorWithRequestInfo(Exception ex, HttpServletRequest request, KeyValueApiResponse errorResponse) {
        String requestUrl = request.getRequestURL().toString();
        log.error(String.format("Exception occurred at URL: %s | Exception: %s | Response: %s", requestUrl, ex, errorResponse));
    }
}
