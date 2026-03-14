package com.github.losion445_max.backend.web.profile.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.github.losion445_max.backend.application.profile.UserProfileService;
import com.github.losion445_max.backend.application.profile.command.AddAddressCommand;
import com.github.losion445_max.backend.application.profile.command.UpdateUserProfileCommand;
import com.github.losion445_max.backend.application.profile.result.AddAddressResult;
import com.github.losion445_max.backend.application.profile.result.GetFullUserProfileResult;
import com.github.losion445_max.backend.application.profile.result.UpdateUserProfileResult;
import com.github.losion445_max.backend.web.profile.dto.AddAddressRequest;
import com.github.losion445_max.backend.web.profile.dto.UpdateUserProfileRequest;
import com.github.losion445_max.backend.web.profile.dto.WebProfileMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserProfileController implements UserProfileApi {
    private final UserProfileService profileService;
    private final WebProfileMapper mapper;

    @Override
    public ResponseEntity<GetFullUserProfileResult> getProfile(String userId) {
        log.info("Get profile for id={}", userId);

        return ResponseEntity.ok(profileService.getFullProfile(UUID.fromString(userId)));
    }

    @Override
    public ResponseEntity<UpdateUserProfileResult> updateProfile(String userId, UpdateUserProfileRequest request) {
        log.info("Update profile for id={}", userId);
        UpdateUserProfileCommand command = mapper.toUpdateCommand(UUID.fromString(userId), request);
        
        return ResponseEntity.ok(profileService.update(command));
    }

    @Override
    public ResponseEntity<AddAddressResult> addAddress(String userId, AddAddressRequest request) {
        log.info("Add address for id={}", userId);
        AddAddressCommand command = mapper.toAddAddressCommand(UUID.fromString(userId), request);

        return ResponseEntity.ok(profileService.addAddress(command));
    }
    
}
