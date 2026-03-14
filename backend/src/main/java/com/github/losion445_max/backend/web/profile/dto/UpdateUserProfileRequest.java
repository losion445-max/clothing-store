package com.github.losion445_max.backend.web.profile.dto;

import java.time.LocalDate;

public record UpdateUserProfileRequest(
    String fullName,
    String phoneNumber,
    LocalDate birthDate
) {

}
