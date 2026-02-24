package com.github.losion445_max.backend.domain.exception;

import com.github.losion445_max.backend.domain.exception.abs.UnauthorizedException;

public class BadCredentialsException extends UnauthorizedException {
    public BadCredentialsException() {
        super("Invalid email or password");
    }
}
