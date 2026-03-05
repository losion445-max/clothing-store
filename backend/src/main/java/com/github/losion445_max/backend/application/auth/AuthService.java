package com.github.losion445_max.backend.application.auth;

import com.github.losion445_max.backend.application.account.RegisterUserUseCase;
import com.github.losion445_max.backend.application.account.command.RegisterUserCommand;
import com.github.losion445_max.backend.application.account.result.RegisterUserResult;
import com.github.losion445_max.backend.application.auth.command.LoginUserCommand;
import com.github.losion445_max.backend.infrastructure.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUseCase;
    private final JwtProvider jwtProvider;

    public AuthResult login(LoginUserCommand command) {
        log.info("Auth for user with email={}", command.getEmail());
        
        RegisterUserResult user = loginUseCase.execute(command);
        String jwtToken = jwtProvider.generateToken(
            user.id(),
            user.email(),
            user.role().name()
        );
        Instant expiresAt = Instant.ofEpochMilli(System.currentTimeMillis() + jwtProvider.getExpires());

        log.info("Auth success");
        
        return AuthResult.builder()
            .accessToken(jwtToken)
            .expires(expiresAt)
            .user(user)
            .build();
    }

    public RegisterUserResult register(RegisterUserCommand command) {
        log.info("Attempting registration for email: {}", command.getEmail());
        
        return registerUserUseCase.execute(command);
    }
}