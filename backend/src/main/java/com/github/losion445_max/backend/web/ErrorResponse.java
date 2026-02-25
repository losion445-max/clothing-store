package com.github.losion445_max.backend.web;

import java.time.Instant;


public record ErrorResponse(
    int status, 
    String code,
    String message,
    String path,
    Instant timestamp
) {
    public ErrorResponse(int status, String code, String message, String path) {
        this(status, code, message, path, Instant.now());
    }
}

