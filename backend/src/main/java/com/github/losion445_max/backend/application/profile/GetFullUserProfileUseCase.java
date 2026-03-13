package com.github.losion445_max.backend.application.profile;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.github.losion445_max.backend.application.profile.result.GetFullUserProfileResult;
import com.github.losion445_max.backend.domain.account.User;
import com.github.losion445_max.backend.domain.account.UserRepository;
import com.github.losion445_max.backend.domain.exception.UserDomainException;
import com.github.losion445_max.backend.domain.profile.UserProfile;
import com.github.losion445_max.backend.domain.profile.UserProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetFullUserProfileUseCase {
    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;

    public GetFullUserProfileResult execute(UUID id) {
        log.info("Get full user profile use case for user with id {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(
                () -> new UserDomainException("There is not such user")
            );

        UserProfile profile = profileRepository.findById(id)
            .orElseGet(() -> {
                log.debug("Profile not found with id {}, return new profile", id);
                return UserProfile.builder().id(id).build();
            });

        return GetFullUserProfileResult.from(user, profile);
    }
}
