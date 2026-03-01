package com.github.losion445_max.backend.domain.user.valueObject;

import com.github.losion445_max.backend.domain.exception.UserDomainException;



public record Phone(String value) {
    private static final String E164_REGEX = "^\\+[1-9]\\d{1,14}$";

    public Phone {
        if (value == null || !value.matches(E164_REGEX)) 
            throw new UserDomainException("Invalid phone number. Must start with '+' followed by 10-15 digits");
        
    }
}
