package com.github.losion445_max.backend.application.auth;

import com.github.losion445_max.backend.application.user.LoginUserUseCase;
import com.github.losion445_max.backend.application.user.command.LoginUserCommand;
import com.github.losion445_max.backend.domain.user.model.User;
import com.github.losion445_max.backend.infrastructure.security.JwtProvider;
import com.github.losion445_max.backend.web.auth.AuthResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private LoginUserUseCase loginUserUseCase;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should return full AuthResponse when login is successful")
    void login_Success() {
        LoginUserCommand command = LoginUserCommand.builder()
                .email("test@mail.com")
                .password("password")
                .build();

        User mockUser = User.builder()
                .name("Max")
                .role(User.Role.USER)
                .build();

        String expectedToken = "mocked.jwt.token";
        long expectedExpires = 3600000L;

        when(loginUserUseCase.execute(command)).thenReturn(mockUser);
        when(jwtProvider.generateToken(mockUser)).thenReturn(expectedToken);
        when(jwtProvider.getExpires()).thenReturn(expectedExpires);

        AuthResponse response = authService.login(command);

        assertAll(
            () -> assertEquals(expectedToken, response.getAccessToken()),
            () -> assertEquals("Bearer", response.getTokenType()),
            () -> assertEquals(expectedExpires, response.getExpires()),
            () -> assertEquals("Max", response.getUsername()),
            () -> assertEquals(User.Role.USER, response.getRole())
        );
        
        verify(loginUserUseCase).execute(command);
        verify(jwtProvider).generateToken(mockUser);
    }

    @Test
    @DisplayName("Should propagate exception when LoginUserUseCase fails")
    void login_UseCaseFails() {
        LoginUserCommand command = LoginUserCommand.builder().email("wrong@mail.com").build();
        
        when(loginUserUseCase.execute(any())).thenThrow(new NoSuchElementException("User not found"));

        assertThrows(NoSuchElementException.class, () -> authService.login(command));
        
        verifyNoInteractions(jwtProvider);
    }
}