package com.github.losion445_max.backend.application.profile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.losion445_max.backend.application.profile.command.AddAddressCommand;
import com.github.losion445_max.backend.application.profile.result.AddAddressResult;
import com.github.losion445_max.backend.domain.profile.Address;
import com.github.losion445_max.backend.domain.profile.UserProfile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    
    Address toAddress(AddAddressCommand command);

    @Mapping(target = "addresses", source = "addresses")
    AddAddressResult toAddAddressResult(UserProfile profile);

    AddAddressResult.AddressData toAddressData(Address address);
}