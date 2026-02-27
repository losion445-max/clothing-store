package com.github.losion445_max.backend.application.auth;

import com.github.losion445_max.backend.application.user.LoginUserUseCase;
import com.github.losion445_max.backend.application.user.RegisterUserUseCase;
import com.github.losion445_max.backend.application.user.command.LoginUserCommand;
import com.github.losion445_max.backend.domain.user.model.User;
import com.github.losion445_max.backend.domain.user.model.User.Role;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private LoginUserUseCase loginUserUseCase;
    
    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should return AuthResult when login is successful")
    void login_Success() {
        // GIVEN
        LoginUserCommand command = LoginUserCommand.builder()
                .email("test@mail.com")
                .password("password")
                .build();

        User mockUser = User.builder()
                .name("Max")
                .role(Role.USER)
                .build();

        String expectedToken = "mocked.jwt.token";
        long durationMs = 3600000L;

        when(loginUserUseCase.execute(command)).thenReturn(mockUser);
        when(jwtProvider.generateToken(mockUser)).thenReturn(expectedToken);
        when(jwtProvider.getExpires()).thenReturn(durationMs);

        // WHEN
        AuthResult result = authService.login(command);

        // THEN
        assertAll(
            () -> assertNotNull(result, "Result should not be null"),
            () -> assertEquals(expectedToken, result.accessToken()),
            () -> assertEquals(mockUser, result.user()),
            () -> assertTrue(result.expires().isAfter(Instant.now()), 
                "Expiration time should be in the future"),
            () -> {
                long expectedTimestamp = System.currentTimeMillis() + durationMs;
                long actualTimestamp = result.expires().toEpochMilli();
                assertTrue(Math.abs(expectedTimestamp - actualTimestamp) < 5000, 
                    "Expiration timestamp mismatch");
            }
        );
        
        verify(loginUserUseCase).execute(command);
        verify(jwtProvider).generateToken(mockUser);
    }

    @Test
    @DisplayName("Should propagate exception when LoginUserUseCase fails")
    void login_UseCaseFails() {
        LoginUserCommand command = LoginUserCommand.builder()
                .email("wrong@mail.com")
                .build();
        
        when(loginUserUseCase.execute(any())).thenThrow(new NoSuchElementException("User not found"));

        assertThrows(NoSuchElementException.class, () -> authService.login(command));
        
        verifyNoInteractions(jwtProvider);
    }
}