package com.github.losion445_max.backend.domain.user.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.github.losion445_max.backend.domain.exception.UserDomainException;
import com.github.losion445_max.backend.domain.user.valueObject.Address;
import com.github.losion445_max.backend.domain.user.valueObject.Phone;
import com.github.losion445_max.backend.domain.user.valueObject.ProfileStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserProfile {

    private final UUID id;

    private String fullName;

    private Phone phoneNumber;

    private LocalDate birthDate;

    @Builder.Default
    private final List<Address> addresses = new ArrayList<>();

    private boolean isMarketingAllowed;
    private Instant marketingConsentUpdatedAt;

    // Factory method  always makes correct object
    public static UserProfile create(UUID id) {
        if (id == null) {
            throw new UserDomainException("Id is incorrect. Id cannot be null");
        }

        return UserProfile.builder()
            .id(id)
            .isMarketingAllowed(false)
            .build();
    }


    public void changeFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new UserDomainException("Full name is incorrect. It has to be not empty");
        }

        this.fullName = fullName.trim();
    }

    public void changePhoneNumber(Phone phoneNumber) {
        if (phoneNumber == null) {
            throw new UserDomainException("Phone number is incorrect. It has to be not empty.");
        }

        this.phoneNumber = phoneNumber;
    }

    public void changeBirthDate(LocalDate birthDate) {
        if (birthDate == null || !birthDate.isBefore(LocalDate.now())) {
            throw new UserDomainException("Birth date is incorrect. It has to be not empty and cannot be in the future!");
        }

        this.birthDate = birthDate;
    }


    public void addAddress(Address address) {
        if (address == null) {
            throw new UserDomainException("Address is incorrect. It has to be not empty.");
        }
        if (addresses.contains(address)) {
            throw new UserDomainException("Address already exists");
        }

        if (addresses.isEmpty()) {
            address = address.withPrimary(true);
        }

        if (address.isPrimary()) {
            addresses.replaceAll(a -> a.withPrimary(false));
        }

        addresses.add(address);

        recalculatePrimaryIfNeeded();
    }

    public void removeAddress(Address address) {
        if (address == null) {
            throw new UserDomainException("Address cannot be null");
        }
        if (addresses.isEmpty()) {
            throw new UserDomainException("Addresses list is empty");
        }
        if (!addresses.contains(address)) {
            throw new UserDomainException("There is no such address in the profile");
        }

        boolean wasPrimary = address.isPrimary();
        addresses.remove(address);

        if (wasPrimary && !addresses.isEmpty()) {
            Address first = addresses.get(0);
            addresses.set(0, first.withPrimary(true));
        }
    }

    public List<Address> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    private void recalculatePrimaryIfNeeded() {
        if (addresses.stream().noneMatch(Address::isPrimary) && !addresses.isEmpty()) {
            Address first = addresses.get(0);
            addresses.set(0, first.withPrimary(true));
        }
    }


    public void allowMarketing() {
        this.isMarketingAllowed = true;
        this.marketingConsentUpdatedAt = Instant.now();
    }

    public void revokeMarketing() {
        this.isMarketingAllowed = false;
        this.marketingConsentUpdatedAt = Instant.now();
    }


    public ProfileStatus getStatus() {
        boolean hasCoreData = fullName != null && phoneNumber != null && birthDate != null;
        boolean hasAddress = !addresses.isEmpty();

        if (hasCoreData && hasAddress) {
            return ProfileStatus.ACTIVE;
        } else if (hasCoreData) {
            return ProfileStatus.INCOMPLETE;
        } else {
            return ProfileStatus.NEW;
        }
    }
}