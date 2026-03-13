package com.github.losion445_max.backend.application.profile.result;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.github.losion445_max.backend.domain.account.User;
import com.github.losion445_max.backend.domain.profile.Address;
import com.github.losion445_max.backend.domain.profile.Phone;
import com.github.losion445_max.backend.domain.profile.UserProfile;

public record GetFullUserProfileResult(
    UUID id,
    String email,
    String name,
    User.Role role,

    String fullName,
    Phone phoneNumber,
    LocalDate birthDate,
    List<Address> addresses,
    boolean isMarketingAllowed
) {
    public static GetFullUserProfileResult from(User user, UserProfile profile) {
        return new GetFullUserProfileResult(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole(),
            profile.getFullName(),
            profile.getPhoneNumber(),
            profile.getBirthDate(),
            profile.getAddresses(),
            profile.isMarketingAllowed()
        );
    }
}
