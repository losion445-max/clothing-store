package com.github.losion445_max.backend.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import com.github.losion445_max.backend.domain.user.model.UserProfile;

public interface UserProfileRepository {

    Optional<UserProfile> findById(UUID id);

    void save(UserProfile profile);

    boolean existsById(UUID id);
    
}
