package com.github.losion445_max.backend.application.auth;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.losion445_max.backend.application.account.result.RegisterUserResult;
import com.github.losion445_max.backend.application.auth.command.LoginUserCommand;
import com.github.losion445_max.backend.domain.account.User;
import com.github.losion445_max.backend.domain.account.UserRepository;
import com.github.losion445_max.backend.domain.exception.BadCredentialsException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@AllArgsConstructor
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    

    public RegisterUserResult execute(LoginUserCommand command) {
        log.info("Login use case started for: {}", command.getEmail());

        User user = userRepository.findByEmail(command.getEmail())
            .orElseThrow(() -> {
                log.warn("User with email={} not found", command.getEmail());

                return new BadCredentialsException("Invalid email or password");
            });

        log.info("User with email={} was found, checking password", command.getEmail());
        if (!passwordEncoder.matches(command.getPassword(), user.getHashPassword())) {
            log.warn("Password for user with email={} is incorrect", command.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        log.info("User with email={} was successfully logged in", user.getEmail());


        return new RegisterUserResult(
            user.getId(), 
            user.getEmail(), 
            user.getName(),
            user.getRole()
        );
    }
    
}
