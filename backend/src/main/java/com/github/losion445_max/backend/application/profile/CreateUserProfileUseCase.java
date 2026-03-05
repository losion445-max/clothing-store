package com.github.losion445_max.backend.application.profile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.losion445_max.backend.application.profile.command.CreateUserProfileCommand;
import com.github.losion445_max.backend.application.profile.result.CreateUserProfileResult;
import com.github.losion445_max.backend.domain.profile.UserProfile;
import com.github.losion445_max.backend.domain.profile.UserProfileRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor 
public class CreateUserProfileUseCase {

    private final UserProfileRepository repository;

    @Transactional
    public CreateUserProfileResult execute(CreateUserProfileCommand command) {
        log.info("Create User profile use case started for {}", command.id());
        if (repository.existsById(command.id())) {
            log.warn("User already exists");
            throw new RuntimeException("User profile already exists"); // todo specific exceptions for UserProfile
        }

        UserProfile profile = UserProfile.create(command.id());
        repository.save(profile);

        CreateUserProfileResult result = new CreateUserProfileResult(command.id());
        log.info("User profile with id={} was successfully created", result.id());
        return result;
        
    }
}
