package com.github.losion445_max.backend.application.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.github.losion445_max.backend.application.user.command.RegisterUserCommand;
import com.github.losion445_max.backend.domain.user.model.User;
import com.github.losion445_max.backend.domain.user.repository.UserRepository;

@Tag("unit")
@ActiveProfiles("test")
public class RegisterUserUseCaseTest {
    
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        useCase = new RegisterUserUseCase(userRepository, passwordEncoder);
    }

    @Test
    void testRegisterUserHappyPath() {
        RegisterUserCommand command = RegisterUserCommand.builder()
            .name("Name")
            .email("email@gmail.com")
            .password("password")
            .build();
        
        when(userRepository.existsByEmail("email@gmail.com"))
            .thenReturn(false);

        when(passwordEncoder.encode("password"))
            .thenReturn("hashPassword");
        
        User savedUser = User.create("Name", "email@gmail.com", "hashPassword");
        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);
        
        User result = useCase.execute(command);

        assertEquals("Name", result.getName());
        assertEquals("email@gmail.com", result.getEmail());
        assertEquals("hashPassword", result.getHashPassword());

        verify(userRepository).existsByEmail("email@gmail.com");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUserEmailAlreadyExists() {
        RegisterUserCommand command = RegisterUserCommand.builder()
            .name("Name")
            .email("email@gmail.com")
            .password("password")
            .build();
        
        when(userRepository.existsByEmail("email@gmail.com")).thenReturn(true);

        IllegalArgumentException exc = assertThrows(
            IllegalArgumentException.class,
            () -> useCase.execute(command));

        assertEquals("Email is already in use", exc.getMessage());

        verify(userRepository).existsByEmail("email@gmail.com");
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}
