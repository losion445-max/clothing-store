package com.github.losion445_max.backend.application.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.losion445_max.backend.application.user.command.UpdateUserProfileCommand;
import com.github.losion445_max.backend.application.user.dto.UpdateUserProfileResult;
import com.github.losion445_max.backend.domain.exception.UserDomainException;
import com.github.losion445_max.backend.domain.user.model.UserProfile;
import com.github.losion445_max.backend.domain.user.repository.UserProfileRepository;
import com.github.losion445_max.backend.domain.user.valueObject.Phone;

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
