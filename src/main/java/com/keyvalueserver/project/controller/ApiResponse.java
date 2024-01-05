package com.keyvalueserver.project.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ApiResponse {
    private final boolean success;
    private final String message;
    private final String data;
}
