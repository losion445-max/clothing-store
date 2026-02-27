package com.github.losion445_max.backend.domain.exception;

import com.github.losion445_max.backend.domain.exception.abs.ConflictException;

public class EmailAlreadyExistsException extends ConflictException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
    
}
