package com.keyvalueserver.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private final String jwttoken;
}