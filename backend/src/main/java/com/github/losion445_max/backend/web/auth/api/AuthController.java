package com.github.losion445_max.backend.web.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.github.losion445_max.backend.application.auth.AuthResult;
import com.github.losion445_max.backend.application.auth.AuthService;
import com.github.losion445_max.backend.application.user.command.LoginUserCommand;
import com.github.losion445_max.backend.application.user.command.RegisterUserCommand;
import com.github.losion445_max.backend.domain.user.model.User;
import com.github.losion445_max.backend.web.auth.dto.AuthMapper;
import com.github.losion445_max.backend.web.auth.dto.AuthRequest;
import com.github.losion445_max.backend.web.auth.dto.AuthResponse;
import com.github.losion445_max.backend.web.user.dto.RegisterUserRequest;
import com.github.losion445_max.backend.web.user.dto.UserResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@AllArgsConstructor
public class AuthController implements AuthApi {

    private final AuthMapper authMapper;
    private final AuthService authService;

    @Override
    public ResponseEntity<UserResponse> register(RegisterUserRequest request) {
        log.info("REST request to register user: {}", request.getEmail());
        RegisterUserCommand command = authMapper.toCommand(request);

        User user = authService.register(command);
        UserResponse response = authMapper.toUserResponse(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<AuthResponse> login(AuthRequest request) {
        log.info("REST request to login user: {}", request.getEmail());

        LoginUserCommand command = authMapper.toCommand(request);
        AuthResult result = authService.login(command);

        return  ResponseEntity.ok(authMapper.toAuthResponse(result));
    }
    
    
}
