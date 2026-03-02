package com.github.losion445_max.backend.application.user.dto;


import java.util.List;
import java.util.UUID;

import com.github.losion445_max.backend.domain.user.valueObject.ProfileStatus;

public record AddAddressResult(
    UUID id, 
    List<AddressData> addresses,
    ProfileStatus status
) {
    public record AddressData(
        String label,
        String city,
        String streetLine,
        boolean isPrimary
    ) {}
}
