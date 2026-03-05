package com.github.losion445_max.backend.application.account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.losion445_max.backend.application.account.command.RegisterUserCommand;
import com.github.losion445_max.backend.application.account.result.RegisterUserResult;
import com.github.losion445_max.backend.domain.account.User;
import com.github.losion445_max.backend.domain.account.UserRepository;
import com.github.losion445_max.backend.domain.exception.EmailAlreadyExistsException;

@Slf4j
@Service
@AllArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public RegisterUserResult execute(RegisterUserCommand command) {
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
        log.info("User saved with id={}: ", savedUser.getId());
        
        return RegisterUserResult.fromDomain(savedUser);

    }
    
}
