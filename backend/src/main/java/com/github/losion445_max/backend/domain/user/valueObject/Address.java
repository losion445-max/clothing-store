package com.github.losion445_max.backend.domain.user.valueObject;

import com.github.losion445_max.backend.domain.exception.UserDomainException;


public record Address(
    String label,
    String countryCode,
    String city,
    String postalCode,
    String streetLine,
    boolean isPrimary
) {
    public Address {
        validateString(label, "Label", 1, 50);
        validateCountryCode(countryCode);
        validateString(city, "City", 2, 100);
        validateString(postalCode, "Postal code", 3, 20);
        validateString(streetLine, "Street line", 5, 255);
    }

    private void validateString(String value, String fieldName, int min, int max) {
        if (value == null || value.isBlank()) {
            throw new UserDomainException(fieldName + " cannot be empty");
        }
        if (value.trim().length() < min || value.trim().length() > max) {
            throw new UserDomainException(fieldName + " length must be between " + min + " and " + max);
        }
    }

    private void validateCountryCode(String code) {
        if (code == null || code.trim().length() != 2) {
            throw new UserDomainException("Country code must be exactly 2 symbols (ISO)");
        }
    }

    public Address withPrimary(boolean primaryStatus) {
        return new Address(label, countryCode, city, postalCode, streetLine, primaryStatus);
    }
}