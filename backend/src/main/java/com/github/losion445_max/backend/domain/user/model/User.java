package com.github.losion445_max.backend.domain.user.model;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private UUID id;
    
    private String name;

    private String email;

    private String hashPassword;

    @Builder.Default
    private Role role = Role.USER;

    private Instant createdAt;
    private Instant updatedAt;

    public static User create(String name, String email, String hashPassword) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email is invalid");
        }
        if (hashPassword == null || hashPassword.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        return User.builder()
            .name(name)
            .email(email)
            .hashPassword(hashPassword)
            .build();
        }

    public enum Role {
        ADMIN,
        USER
    }

}


