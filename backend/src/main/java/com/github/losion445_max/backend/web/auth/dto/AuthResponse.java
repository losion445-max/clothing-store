package com.github.losion445_max.backend.web.auth.dto;

import com.github.losion445_max.backend.domain.user.model.User.Role;

import lombok.Builder;


@Builder
public record AuthResponse(
    String accessToken,
    String tokenType, 
    long expires,
    UserViewInfo user
) {
    public record UserViewInfo(
        String username,
        Role role
    ) {}
}
