package com.github.losion445_max.backend.application.profile.command;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserProfileCommand(UUID id, String fullName, String phoneNumber, LocalDate birthDate) {

}
