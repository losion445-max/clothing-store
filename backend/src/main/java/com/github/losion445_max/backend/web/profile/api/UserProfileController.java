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

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class UserProfileController implements UserProfileApi {
    private final UserProfileService profileService;

    @Override
    public ResponseEntity<GetFullUserProfileResult> getProfile(String userId) {
        return ResponseEntity.ok(profileService.getFullProfile(UUID.fromString(userId)));
    }

    @Override
    public ResponseEntity<UpdateUserProfileResult> updateProfile(String userId, UpdateUserProfileCommand command) {
        command = UpdateUserProfileCommand.withId(UUID.fromString(userId), command);
        
        return ResponseEntity.ok(profileService.update(command));
    }

    @Override
    public ResponseEntity<AddAddressResult> addAddress(String userId, AddAddressCommand command) {
        command = AddAddressCommand.withId(UUID.fromString(userId), command);

        return ResponseEntity.ok(profileService.addAddress(command));
    }
    
}
