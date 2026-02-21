package com.github.losion445_max.backend.domain.user.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String hashPassword;

    @Builder.Default
    private Role role = Role.USER;

    private Instant createdAt;
    private Instant updatedAt;

    public enum Role {
        ADMIN,
        USER
    }
}


