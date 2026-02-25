package com.github.losion445_max.backend.web;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.github.losion445_max.backend.domain.exception.BadCredentialsException;
import com.github.losion445_max.backend.domain.exception.UserDomainException;
import com.github.losion445_max.backend.domain.exception.abs.BaseException;
import com.github.losion445_max.backend.domain.exception.abs.ConflictException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<Class<? extends BaseException>, HttpStatus> STATUS_MAP = Map.of(
        ConflictException.class, HttpStatus.CONFLICT,
        UserDomainException.class, HttpStatus.BAD_REQUEST,
        BadCredentialsException.class, HttpStatus.UNAUTHORIZED
    );
    private ResponseEntity<ErrorResponse> build(HttpStatus s, String code, String msg, HttpServletRequest req) {
        log.warn("{} error {} at {}", code, msg, req.getRequestURI());

        ErrorResponse res = new ErrorResponse(s.value(), code, msg, req.getRequestURI());
        
        return ResponseEntity.status(s).body(res);
    }


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(BaseException exc, HttpServletRequest request) {
        HttpStatus status = STATUS_MAP.entrySet().stream()
            .filter(entry -> entry.getKey().isInstance(exc))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        String code = exc.getClass().getSimpleName().replace("Exception", "")
                        .replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();

        return build(status, code, exc.getMessage(), request);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exc, HttpServletRequest request) {
        String details = exc.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        log.warn("Validation exception: {}", exc.getMessage());

        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", details, request);

    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException exc, HttpServletRequest request) {
        log.warn("Path not found: {}", exc.getRequestURL());

        return build(HttpStatus.NOT_FOUND, "PATH_NOT_FOUND", "Resource not found", request);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception exc, HttpServletRequest request) {
        log.error("Unhandled exception at {}: ", request.getRequestURI(), exc);
        
        return build(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "INTERNAL_SERVER_ERROR", 
            "An unexpected error occurred", 
            request
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException exc, 
            HttpServletRequest request) {
        
        log.warn("Method {} not supported at {}", exc.getMethod(), request.getRequestURI());
        
        return build(
            HttpStatus.METHOD_NOT_ALLOWED,
            "METHOD_NOT_ALLOWED", 
            "Supported methods: " + java.util.Arrays.toString(exc.getSupportedMethods()), 
            request
        );
    }
}


