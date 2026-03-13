package com.github.losion445_max.backend.application.profile.result;

import java.util.UUID;

import com.github.losion445_max.backend.domain.profile.ProfileStatus;

public record UpdateUserProfileResult(UUID id, ProfileStatus status) {

    public static UpdateUserProfileResult from(UUID id, ProfileStatus status) {
        return new UpdateUserProfileResult(id, status);
    }
}
