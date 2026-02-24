package com.github.losion445_max.backend.application.user;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.losion445_max.backend.application.user.command.LoginUserCommand;
import com.github.losion445_max.backend.domain.exception.BadCredentialsException;
import com.github.losion445_max.backend.domain.user.model.User;
import com.github.losion445_max.backend.domain.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@AllArgsConstructor
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    

    public User execute(LoginUserCommand command) {
        log.info("Login use case started for: {}", command.getEmail());

        User user = userRepository.findByEmail(command.getEmail())
            .orElseThrow(() -> {
                log.warn("User with email={} not found", command.getEmail());

                return new BadCredentialsException();
            });

        log.info("User with email={} was found, checking password", command.getEmail());
        if (!passwordEncoder.matches(command.getPassword(), user.getHashPassword())) {
            log.warn("Password for user with email={} is incorrect", command.getEmail());
            throw new BadCredentialsException();
        }

        log.info("User with email={} was successfully logged in", user.getEmail());
        return user;
    }
    
}
