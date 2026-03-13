package com.github.losion445_max.backend.application.profile;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.losion445_max.backend.application.profile.command.AddAddressCommand;
import com.github.losion445_max.backend.application.profile.mapper.ProfileMapper;
import com.github.losion445_max.backend.application.profile.result.AddAddressResult;
import com.github.losion445_max.backend.domain.exception.UserDomainException;
import com.github.losion445_max.backend.domain.profile.Address;
import com.github.losion445_max.backend.domain.profile.UserProfile;
import com.github.losion445_max.backend.domain.profile.UserProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddAddressUserProfileCase {
    private final UserProfileRepository repository;
    private final ProfileMapper profileMapper;

    @Transactional
    public AddAddressResult execute(AddAddressCommand command) {
        log.info("Add address profile use case started for {}", command.id());   

        UserProfile profile = repository.findById(command.id())
            .orElseThrow(() -> new UserDomainException("User Profile not found"));

        Address address = profileMapper.toAddress(command);
        profile.addAddress(address);

        repository.update(profile);
        log.info("Address successfully added {}", address.label());

        return profileMapper.toAddAddressResult(profile);


    }
}
