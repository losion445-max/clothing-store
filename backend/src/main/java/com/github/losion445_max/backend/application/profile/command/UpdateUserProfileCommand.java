package com.github.losion445_max.backend.application.profile.command;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserProfileCommand(UUID id, String fullName, String phoneNumber, LocalDate birthDate) {

    public static UpdateUserProfileCommand withId(UUID id, UpdateUserProfileCommand command) {
        return new UpdateUserProfileCommand(
            id,
            command.fullName,
            command.phoneNumber,
            command.birthDate
        );
    }
}
