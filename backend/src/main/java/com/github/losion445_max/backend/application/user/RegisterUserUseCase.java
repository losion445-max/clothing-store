package com.github.losion445_max.backend.application.user;

import com.github.losion445_max.backend.domain.user.repository.UserRepository;
import com.github.losion445_max.backend.web.user.dto.RegisterUserRequest;

import lombok.AllArgsConstructor;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.losion445_max.backend.domain.user.model.User;

@Service
@AllArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public User execute(RegisterUserRequest userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = User.builder()
        .name(userDTO.getName())
        .email(userDTO.getEmail())
        .hashPassword(passwordEncoder.encode(userDTO.getPassword()))
        .build();

        return userRepository.save(user);

    }
    
}
