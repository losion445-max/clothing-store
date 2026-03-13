package com.github.losion445_max.backend.application.auth.result;

import java.time.Instant;

import com.github.losion445_max.backend.application.account.result.RegisterUserResult;

import lombok.Builder;

@Builder
public record AuthResult (
    String accessToken,
    Instant expires,
    RegisterUserResult user
) {}