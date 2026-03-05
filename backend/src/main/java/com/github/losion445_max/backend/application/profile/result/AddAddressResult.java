package com.github.losion445_max.backend.application.profile.result;


import java.util.List;
import java.util.UUID;

import com.github.losion445_max.backend.domain.profile.ProfileStatus;

public record AddAddressResult(
    UUID id, 
    List<AddressData> addresses,
    ProfileStatus status
) {
    public record AddressData(
        String label,
        String countryCode,
        String city,
        String postalCode,
        String streetLine,
        boolean isPrimary
    ) {}
}
