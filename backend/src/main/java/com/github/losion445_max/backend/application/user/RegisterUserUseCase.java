package com.github.losion445_max.backend.application.user;

import com.github.losion445_max.backend.domain.user.repository.UserRepository;
import com.github.losion445_max.backend.web.user.dto.RegisterUserRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.losion445_max.backend.domain.user.model.User;

@Slf4j
@Service
@AllArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public User execute(RegisterUserRequest userDTO) {
        log.info("Register use case starter for: " + userDTO);

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            log.warn("Email {} is already in use", userDTO.getEmail());
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = User.create(
            userDTO.getName(),
             userDTO.getEmail(),
             passwordEncoder.encode(userDTO.getPassword())
        );

        User savedUser = userRepository.save(user);
        log.info("User saved with id={}: ", user.getId());

        return savedUser;

    }
    
}
