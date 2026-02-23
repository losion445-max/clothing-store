package com.github.losion445_max.backend.web.auth;

import com.github.losion445_max.backend.domain.user.model.User.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private final String accessToken;
    private final String tokenType;
    private final long expires;
    private final String username;
    private final Role role;
    
}
