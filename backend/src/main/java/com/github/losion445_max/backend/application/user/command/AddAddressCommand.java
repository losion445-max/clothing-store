package com.github.losion445_max.backend.application.user.command;

import java.util.UUID;

public record AddAddressCommand(
    UUID id,
    String label,
    String countryCode,
    String city,
    String postalCode,
    String streetLine,
    boolean isPrimary
) {

}
