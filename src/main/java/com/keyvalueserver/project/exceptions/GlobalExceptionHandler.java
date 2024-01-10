package com.keyvalueserver.project.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.IOException;
import com.keyvalueserver.project.model.ApiResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KeyNotFoundException.class)
    public ResponseEntity<ApiResponse> handleKeyNotFoundException(KeyNotFoundException ex, HttpServletRequest request) {
        ApiResponse apiResponse = new ApiResponse(false, "Key not found", null);
        logErrorWithRequestInfo(ex, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        ApiResponse errorResponse = new ApiResponse(false, "key or value cannot be null", null);
        logErrorWithRequestInfo(ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse> handleIOException(IOException ex, HttpServletRequest request) {
        ApiResponse errorResponse = new ApiResponse(false, "Error reading request body", null);
        logErrorWithRequestInfo(ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        ApiResponse errorResponse = new ApiResponse(false, "Internal server error", null);
        logErrorWithRequestInfo(ex, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private void logErrorWithRequestInfo(Exception ex, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        log.error("Exception occurred at URL: " + requestUrl, ex);
    }
}
