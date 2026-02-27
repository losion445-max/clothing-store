package com.github.losion445_max.backend.web.user.dto;

import java.util.UUID;

import com.github.losion445_max.backend.domain.user.model.User.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String email;
    private String name;
    private Role role;
}
