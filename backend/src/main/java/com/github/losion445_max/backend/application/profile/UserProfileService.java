package com.github.losion445_max.backend.application.profile;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.github.losion445_max.backend.application.profile.command.AddAddressCommand;
import com.github.losion445_max.backend.application.profile.command.UpdateUserProfileCommand;
import com.github.losion445_max.backend.application.profile.result.AddAddressResult;
import com.github.losion445_max.backend.application.profile.result.CreateUserProfileResult;
import com.github.losion445_max.backend.application.profile.result.GetFullUserProfileResult;
import com.github.losion445_max.backend.application.profile.result.UpdateUserProfileResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserProfileService {
    private final GetFullUserProfileUseCase getFullUserProfileUseCase;
    private final CreateUserProfileUseCase createUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final AddAddressUserProfileCase addAddressUserProfileCase;

    public CreateUserProfileResult create(UUID id) {
        return createUserProfileUseCase.execute(id);
    }

    public GetFullUserProfileResult getFullProfile(UUID id) {
        return getFullUserProfileUseCase.execute(id);
    }

    public UpdateUserProfileResult update(UpdateUserProfileCommand command) {
        return updateUserProfileUseCase.execute(command);
    } 

    public AddAddressResult addAddress(AddAddressCommand command) {
        return addAddressUserProfileCase.execute(command);
    }
}
