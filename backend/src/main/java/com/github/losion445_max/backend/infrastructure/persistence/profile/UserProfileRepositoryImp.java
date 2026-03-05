package com.github.losion445_max.backend.infrastructure.persistence.profile;

import com.github.losion445_max.backend.domain.profile.UserProfile;
import com.github.losion445_max.backend.domain.profile.UserProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryImp implements UserProfileRepository {

    private final SpringDataUserProfileRepository jpaRepository;
    private final UserProfileMapper mapper;

@   Override
    public Optional<UserProfile> findById(UUID id) {
        log.debug("Looking for user profile with id={}", id);
        
        Optional<UserProfileJpaEntity> userProfileOpt = jpaRepository.findById(id);

        if (userProfileOpt.isEmpty()) {
            log.warn("User profile not found with id={}", id);
            return userProfileOpt.map(mapper::toDomain);
        }

        log.info("User profile found with id={}", id);
        return userProfileOpt.map(mapper::toDomain);
    }


    @Override
    public void save(UserProfile profile) {
        log.debug("Saving user profile: {}", profile.getId());

        UserProfileJpaEntity entity = mapper.toEntity(profile);
        jpaRepository.save(entity);

        log.info("User profile saved: {}", profile.getId());
    }
    

    @Override
    public boolean existsById(UUID id) {
        log.debug("Check if user profile with id {} exists", id);
        boolean exists = jpaRepository.existsById(id);
        log.info("User profile exists check for {}: {}", id, exists);

        return exists;
    }
}