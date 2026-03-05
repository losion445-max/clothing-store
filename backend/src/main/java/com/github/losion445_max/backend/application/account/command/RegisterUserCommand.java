package com.github.losion445_max.backend.application.account.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class RegisterUserCommand {
    private final String name;
    private final String email;
    private final String password;
}
