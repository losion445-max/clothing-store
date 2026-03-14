package com.github.losion445_max.backend.web.profile.dto;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.losion445_max.backend.application.profile.command.AddAddressCommand;
import com.github.losion445_max.backend.application.profile.command.UpdateUserProfileCommand;

@Mapper(componentModel = "spring")
public interface WebProfileMapper {

    @Mapping(target = "id", source = "userId")
    UpdateUserProfileCommand toUpdateCommand(UUID userId, UpdateUserProfileRequest request);

    @Mapping(target = "id", source = "userId")
    AddAddressCommand toAddAddressCommand(UUID userId, AddAddressRequest request);
    
}
