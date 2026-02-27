package com.github.losion445_max.backend.domain.exception.abs;


public abstract class BaseException extends RuntimeException{
    protected BaseException(String message) {
        super(message);
    }
}
