package com.github.losion445_max.backend.application.profile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.losion445_max.backend.application.profile.command.UpdateUserProfileCommand;
import com.github.losion445_max.backend.application.profile.result.UpdateUserProfileResult;
import com.github.losion445_max.backend.domain.exception.UserDomainException;
import com.github.losion445_max.backend.domain.profile.Phone;
import com.github.losion445_max.backend.domain.profile.UserProfile;
import com.github.losion445_max.backend.domain.profile.UserProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserProfileUseCase {
    private final UserProfileRepository repository;

    @Transactional
    public UpdateUserProfileResult execute(UpdateUserProfileCommand command) {
        log.info("Update user profile use case started for {}", command.id());

        UserProfile profile = repository.findById(command.id())
            .orElseThrow(
                () -> new UserDomainException("Profile not found"));

        if (command.fullName() != null) 
                profile.changeFullName(command.fullName());
            
        if (command.phoneNumber() != null) 
            profile.changePhoneNumber(new Phone(command.phoneNumber()));
        
        if (command.birthDate()!= null) 
            profile.changeBirthDate(command.birthDate());
        

        repository.save(profile);
        log.info("User profile with id {} was successfully updated", profile.getId());

        UpdateUserProfileResult result = new UpdateUserProfileResult(
            profile.getId(),
            profile.getStatus()
        );

        return result;


    }
}
