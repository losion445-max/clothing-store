package com.github.losion445_max.backend.domain.user.model;

import java.time.Instant;
import java.util.UUID;


import com.github.losion445_max.backend.domain.exception.UserDomainException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User {
    private final UUID id;
    
    private final String name;
    private final String email;
    private final String hashPassword;

    private final Role role;

    private final Instant createdAt;
    private final Instant updatedAt;

    
    public static User create(String name, String email, String hashPassword) {
        if (name == null || name.isBlank()) {
            throw new UserDomainException("Name is required");
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new UserDomainException("Email is invalid");
        }
        if (hashPassword == null || hashPassword.isBlank()) {
            throw new UserDomainException("Password is required");
        }


        return User.builder()
            .name(name)
            .email(email)
            .hashPassword(hashPassword)
            .role(Role.USER)
            .build();
        }

    public enum Role {
        ADMIN,
        USER
    }

}


