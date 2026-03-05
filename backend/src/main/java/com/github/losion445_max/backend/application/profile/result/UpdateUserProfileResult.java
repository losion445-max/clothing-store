package com.github.losion445_max.backend.application.profile.result;

import java.util.UUID;

import com.github.losion445_max.backend.domain.profile.ProfileStatus;

public record UpdateUserProfileResult(UUID id, ProfileStatus status) {

}
