package com.github.losion445_max.backend.application.user.dto;

import java.util.UUID;

import com.github.losion445_max.backend.domain.user.valueObject.ProfileStatus;

public record UpdateUserProfileResult(UUID id, ProfileStatus status) {

}
