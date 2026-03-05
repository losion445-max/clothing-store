package com.github.losion445_max.backend.web.auth.dto;

import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.github.losion445_max.backend.application.account.command.RegisterUserCommand;
import com.github.losion445_max.backend.application.account.result.RegisterUserResult;
import com.github.losion445_max.backend.application.auth.AuthResult;
import com.github.losion445_max.backend.application.auth.command.LoginUserCommand;
import com.github.losion445_max.backend.web.user.dto.RegisterUserRequest;
import com.github.losion445_max.backend.web.user.dto.UserResponse;

@Mapper(componentModel = "spring", imports = {Instant.class})
public interface AuthMapper {

    RegisterUserCommand toCommand(RegisterUserRequest request);
    LoginUserCommand toCommand(AuthRequest request);

    
    UserResponse toUserResponse(RegisterUserResult result);

    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "expires", expression = "java(result.expires().toEpochMilli())")
    @Mapping(target = "user", source = "user")
    AuthResponse toAuthResponse(AuthResult result);

    @Mapping(target = "username", source = "name")
    @Mapping(target = "role", source = "role")
    AuthResponse.UserViewInfo toUserViewInfo(RegisterUserResult result);
}