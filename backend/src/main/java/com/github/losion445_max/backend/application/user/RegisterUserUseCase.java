package com.github.losion445_max.backend.application.user;

import com.github.losion445_max.backend.domain.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.losion445_max.backend.application.user.command.RegisterUserCommand;
import com.github.losion445_max.backend.domain.exception.EmailAlreadyExistsException;
import com.github.losion445_max.backend.domain.user.model.User;

@Slf4j
@Service
@AllArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public User execute(RegisterUserCommand command) {
        log.info("Register use case starter for: {}", command.getEmail());

        if (userRepository.existsByEmail(command.getEmail())) {
            log.warn("Email {} is already in use", command.getEmail());
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        User user = User.create(
            command.getName(),
             command.getEmail(),
             passwordEncoder.encode(command.getPassword())
        );

        User savedUser = userRepository.save(user);
        log.info("User saved with id={}: ", user.getId());

        return savedUser;

    }
    
}
