package com.github.losion445_max.backend.web.profile.dto;


public record AddAddressRequest(
    String label,
    String countryCode,
    String city, 
    String postalCode,
    String streetLine,
    boolean isPrimary
) {}
