package com.github.losion445_max.backend.web.auth.api;

import com.github.losion445_max.backend.web.ErrorResponse;
import com.github.losion445_max.backend.web.auth.dto.AuthRequest;
import com.github.losion445_max.backend.web.auth.dto.AuthResponse;
import com.github.losion445_max.backend.web.user.dto.RegisterUserRequest;
import com.github.losion445_max.backend.web.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication", description = "Endpoints for user identity management, registration, and login")
@RequestMapping(
    path = "/api/auth", 
    produces = MediaType.APPLICATION_JSON_VALUE, 
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public interface AuthApi {

    /**
     * Registers a new user in the system.
     */
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account and returns the created user profile details."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "User successfully created",
            content = @Content(
                schema = @Schema(implementation = UserResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "id": "550e8400-e29b-41d4-a716-446655440000",
                      "email": "senior.dev@example.com",
                      "firstName": "John",
                      "lastName": "Doe"
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation failed",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "status": 400,
                      "code": "BAD_REQUEST",
                      "message": "Password must be at least 8 characters long",
                      "path": "/api/auth/register",
                      "timestamp": "2026-02-27T20:15:00Z"
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email conflict",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "status": 409,
                      "code": "CONFLICT",
                      "message": "User with this email already exists",
                      "path": "/api/auth/register",
                      "timestamp": "2026-02-27T20:15:00Z"
                    }
                    """)
            )
        )
    })
    @PostMapping("/register")
    ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request);

    /**
     * Authenticates a user and provides a JWT token.
     */
    @Operation(
        summary = "Login user",
        description = "Authenticates credentials and returns a JWT access token along with user info."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully authenticated",
            content = @Content(
                schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "user": {
                        "id": "550e8400-e29b-41d4-a716-446655440000",
                        "email": "senior.dev@example.com"
                      }
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Authentication failed",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "status": 403,
                      "code": "FORBIDDEN",
                      "message": "Invalid email or password",
                      "path": "/api/auth/login",
                      "timestamp": "2026-02-27T20:15:00Z"
                    }
                    """)
            )
        )
    })
    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request);
}