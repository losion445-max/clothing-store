package com.github.losion445_max.backend.application.user.command;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
public class RegisterUserCommand {
    private final String name;
    private final String email;
    private final String password;
}
