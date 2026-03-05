package com.github.losion445_max.backend.application.auth.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginUserCommand {
    private final String email;
    private final String password;
}
