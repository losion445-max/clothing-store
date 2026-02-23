package com.github.losion445_max.backend.application.user;

import com.github.losion445_max.backend.application.user.command.LoginUserCommand;
import com.github.losion445_max.backend.domain.user.model.User;
import com.github.losion445_max.backend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Should return user when credentials are valid")
    void execute_Success() {
        String email = "max@example.com";
        String password = "securePassword";
        String hash = "hashed_value";

        LoginUserCommand command = LoginUserCommand.builder()
                .email(email)
                .password(password)
                .build();

        User domainUser = User.builder()
                .id(UUID.randomUUID())
                .name("Max")
                .email(email)
                .hashPassword(hash)
                .role(User.Role.USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(domainUser));
        when(passwordEncoder.matches(password, hash)).thenReturn(true);

        User result = loginUserUseCase.execute(command);

        assertAll(
            () -> assertEquals(domainUser.getEmail(), result.getEmail()),
            () -> assertEquals(domainUser.getId(), result.getId()),
            () -> verify(userRepository).findByEmail(email),
            () -> verify(passwordEncoder).matches(password, hash)
        );
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when user email not found")
    void execute_UserNotFound() {
        LoginUserCommand command = LoginUserCommand.builder()
                .email("unknown@mail.com")
                .password("any")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> loginUserUseCase.execute(command));
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when password does not match")
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

        assertThrows(IllegalArgumentException.class, () -> loginUserUseCase.execute(command));
    }
}