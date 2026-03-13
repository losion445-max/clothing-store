package com.github.losion445_max.backend.application.profile;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.losion445_max.backend.application.profile.result.CreateUserProfileResult;
import com.github.losion445_max.backend.domain.exception.UserDomainException;
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
    public CreateUserProfileResult execute(UUID id) {
        log.info("Create User profile use case started for {}", id);
        if (repository.existsById(id)) {
            log.warn("User already exists");
            throw new UserDomainException("User profile already exists"); // todo specific exceptions for UserProfile
        }

        UserProfile profile = UserProfile.create(id);
        repository.save(profile);

        CreateUserProfileResult result = new CreateUserProfileResult(id);
        log.info("User profile with id={} was successfully created", result.id());
        return result;
        
    }
}
