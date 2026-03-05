package com.github.losion445_max.backend.application.user;

import com.github.losion445_max.backend.application.account.result.RegisterUserResult;
import com.github.losion445_max.backend.application.auth.LoginUserUseCase;
import com.github.losion445_max.backend.application.auth.command.LoginUserCommand;
import com.github.losion445_max.backend.domain.account.User;
import com.github.losion445_max.backend.domain.account.UserRepository;
import com.github.losion445_max.backend.domain.exception.BadCredentialsException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    @Test
    @DisplayName("Should return RegisterUserResult when credentials are valid")
    void execute_Success() {
        String email = "max@example.com";
        String password = "securePassword";
        String hash = "hashed_value";
        UUID userId = UUID.randomUUID();

        LoginUserCommand command = LoginUserCommand.builder()
                .email(email)
                .password(password)
                .build();

        User domainUser = User.builder()
                .id(userId)
                .name("Max")
                .email(email)
                .hashPassword(hash)
                .role(User.Role.USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(domainUser));
        when(passwordEncoder.matches(password, hash)).thenReturn(true);

        RegisterUserResult result = loginUserUseCase.execute(command);

        assertAll(
            () -> assertEquals(email, result.email()),
            () -> assertEquals(userId, result.id()),
            () -> assertEquals("Max", result.name()),
            () -> assertEquals(User.Role.USER, result.role()),
            () -> verify(userRepository).findByEmail(email),
            () -> verify(passwordEncoder).matches(password, hash)
        );
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when user email not found")
    void execute_UserNotFound() {
        LoginUserCommand command = LoginUserCommand.builder()
                .email("unknown@mail.com")
                .password("any")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> loginUserUseCase.execute(command));
        verify(userRepository).findByEmail("unknown@mail.com");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when password does not match")
    void execute_WrongPassword() {
        String email = "max@example.com";
        LoginUserCommand command = LoginUserCommand.builder()
                .email(email)
                .password("wrong_pass")
                .build();

        User domainUser = User.builder()
                .email(email)
                .hashPassword("correct_hash")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(domainUser));
        when(passwordEncoder.matches("wrong_pass", "correct_hash")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> loginUserUseCase.execute(command));
    }
}