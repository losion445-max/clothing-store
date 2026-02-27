package com.github.losion445_max.backend.domain.exception;

import com.github.losion445_max.backend.domain.exception.abs.BadRequestException;


public class UserDomainException extends BadRequestException {
    public UserDomainException(String message) {
        super(message);
    }
}
