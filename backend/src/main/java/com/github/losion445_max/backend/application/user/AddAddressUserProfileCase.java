package com.github.losion445_max.backend.application.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.losion445_max.backend.application.user.command.AddAddressCommand;
import com.github.losion445_max.backend.application.user.dto.AddAddressResult;
import com.github.losion445_max.backend.domain.exception.UserDomainException;
import com.github.losion445_max.backend.domain.user.model.UserProfile;
import com.github.losion445_max.backend.domain.user.repository.UserProfileRepository;
import com.github.losion445_max.backend.domain.user.valueObject.Address;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddAddressUserProfileCase {
    private final UserProfileRepository repository;

    @Transactional
    public AddAddressResult execute(AddAddressCommand command) {
        log.info("Add address profile use case started for {}", command.id());   

        UserProfile profile = repository.findById(command.id())
            .orElseThrow(() -> new UserDomainException("User Profile not found"));

        Address address = new Address(
            command.label(),
            command.countryCode(),
            command.city(),
            command.postalCode(),
            command.streetLine(),
            command.isPrimary()
        );
        profile.addAddress(address);

        repository.save(profile);
        log.info("Address successfully added {}", address.label());

        
        List<AddAddressResult.AddressData> addressDataList = profile.getAddresses().stream()
            .map(addr -> new AddAddressResult.AddressData(
                addr.label(),
                addr.countryCode(),
                addr.city(),
                addr.postalCode(),
                addr.streetLine(),
                addr.isPrimary()
        )).toList();

        AddAddressResult result = new AddAddressResult(
            profile.getId(),
            addressDataList,
            profile.getStatus()
        );
        return result;


    }
}
