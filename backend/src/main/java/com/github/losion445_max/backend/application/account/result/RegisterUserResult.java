package com.github.losion445_max.backend.application.account.result;

import java.util.UUID;

import com.github.losion445_max.backend.domain.account.User;
import com.github.losion445_max.backend.domain.account.User.Role;

public record RegisterUserResult (
    UUID id,
    String email,
    String name,
    Role role
) {
    public static RegisterUserResult fromDomain(User user) {
        return new RegisterUserResult(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole()
        );
    }
}
