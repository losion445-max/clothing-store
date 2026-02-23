package com.github.losion445_max.backend.application.auth;

import org.springframework.stereotype.Service;

import com.github.losion445_max.backend.application.user.LoginUserUseCase;
import com.github.losion445_max.backend.application.user.command.LoginUserCommand;
import com.github.losion445_max.backend.domain.user.model.User;
import com.github.losion445_max.backend.infrastructure.security.JwtProvider;
import com.github.losion445_max.backend.web.auth.AuthResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private LoginUserUseCase useCase;
    private JwtProvider jwtProvider;


    public AuthResponse login(LoginUserCommand command) {
        log.info("Auth for user with email={}", command.getEmail());
        User user = useCase.execute(command);

        String jwtToken = jwtProvider.generateToken(user);

        log.info("Auth success");
        return AuthResponse.builder()
            .accessToken(jwtToken)
            .tokenType("Bearer")
            .expires(jwtProvider.getExpires())
            .username(user.getName())
            .role(user.getRole())
            .build();
    }
}
