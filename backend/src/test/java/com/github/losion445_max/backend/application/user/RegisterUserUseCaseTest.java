package com.github.losion445_max.backend.application.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.losion445_max.backend.application.account.RegisterUserUseCase;
import com.github.losion445_max.backend.application.account.command.RegisterUserCommand;
import com.github.losion445_max.backend.application.account.result.RegisterUserResult;
import com.github.losion445_max.backend.domain.account.User;
import com.github.losion445_max.backend.domain.account.UserRepository;
import com.github.losion445_max.backend.domain.exception.EmailAlreadyExistsException;

@Tag("unit")
class RegisterUserUseCaseTest {
    
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        useCase = new RegisterUserUseCase(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("Should return RegisterUserResult when registration is successful")
    void testRegisterUserHappyPath() {
        RegisterUserCommand command = RegisterUserCommand.builder()
            .name("Name")
            .email("email@gmail.com")
            .password("password")
            .build();
        
        when(userRepository.existsByEmail("email@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashPassword");
        
        User savedUser = User.create("Name", "email@gmail.com", "hashPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        RegisterUserResult result = useCase.execute(command);

        assertEquals("Name", result.name());
        assertEquals("email@gmail.com", result.email());
        assertEquals(User.Role.USER, result.role());

        verify(userRepository).existsByEmail("email@gmail.com");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email is taken")
    void testRegisterUserEmailAlreadyExists() {
        RegisterUserCommand command = RegisterUserCommand.builder()
            .name("Name")
            .email("email@gmail.com")
            .password("password")
            .build();
        
        when(userRepository.existsByEmail("email@gmail.com")).thenReturn(true);

        EmailAlreadyExistsException exc = assertThrows(
            EmailAlreadyExistsException.class,
            () -> useCase.execute(command));

        assertEquals("Email is already in use", exc.getMessage());

        verify(userRepository).existsByEmail("email@gmail.com");
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}