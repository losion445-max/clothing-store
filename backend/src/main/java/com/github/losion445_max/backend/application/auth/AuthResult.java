package com.github.losion445_max.backend.application.auth;

import java.time.Instant;

import com.github.losion445_max.backend.domain.user.model.User;
import lombok.Builder;


@Builder
public record AuthResult(
    String accessToken,
    Instant expires,
    User user
) {}