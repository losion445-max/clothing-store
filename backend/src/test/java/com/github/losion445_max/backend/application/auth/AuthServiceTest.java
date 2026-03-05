package com.github.losion445_max.backend.application.auth;

import com.github.losion445_max.backend.application.account.RegisterUserUseCase;
import com.github.losion445_max.backend.application.account.command.RegisterUserCommand;
import com.github.losion445_max.backend.application.account.result.RegisterUserResult;
import com.github.losion445_max.backend.application.auth.command.LoginUserCommand;
import com.github.losion445_max.backend.domain.account.User.Role;
import com.github.losion445_max.backend.infrastructure.security.JwtProvider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private LoginUserUseCase loginUseCase;
    
    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should return AuthResult when login is successful")
    void login_Success() {
        UUID userId = UUID.randomUUID();
        LoginUserCommand command = LoginUserCommand.builder()
                .email("test@mail.com")
                .password("password")
                .build();

        RegisterUserResult mockResult = new RegisterUserResult(
                userId,
                "test@mail.com",
                "Max",
                Role.USER
        );

        String expectedToken = "mocked.jwt.token";
        long durationMs = 3600000L;

        when(loginUseCase.execute(command)).thenReturn(mockResult);
        when(jwtProvider.generateToken(userId, "test@mail.com", Role.USER.name()))
                .thenReturn(expectedToken);
        when(jwtProvider.getExpires()).thenReturn(durationMs);

        AuthResult result = authService.login(command);

        assertAll(
            () -> assertNotNull(result, "Result should not be null"),
            () -> assertEquals(expectedToken, result.accessToken()),
            () -> assertEquals(mockResult, result.user()),
            () -> assertTrue(result.expires().isAfter(Instant.now()), 
                "Expiration time should be in the future")
        );
        
        verify(loginUseCase).execute(command);
        verify(jwtProvider).generateToken(userId, "test@mail.com", Role.USER.name());
    }

    @Test
    @DisplayName("Should return RegisterUserResult when registration is successful")
    void register_Success() {
        RegisterUserCommand command = new RegisterUserCommand("Max", "test@mail.com", "password");
        RegisterUserResult expectedResult = new RegisterUserResult(UUID.randomUUID(), "test@mail.com", "Max", Role.USER);
        
        when(registerUserUseCase.execute(command)).thenReturn(expectedResult);

        RegisterUserResult actualResult = authService.register(command);

        assertEquals(expectedResult, actualResult);
        verify(registerUserUseCase).execute(command);
    }

    @Test
    @DisplayName("Should propagate exception when LoginUserUseCase fails")
    void login_UseCaseFails() {
        LoginUserCommand command = LoginUserCommand.builder()
                .email("wrong@mail.com")
                .build();
        
        when(loginUseCase.execute(any())).thenThrow(new NoSuchElementException("User not found"));

        assertThrows(NoSuchElementException.class, () -> authService.login(command));
        
        verifyNoInteractions(jwtProvider);
    }
}