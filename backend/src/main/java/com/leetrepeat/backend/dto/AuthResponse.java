package com.leetrepeat.backend.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String message;
}
