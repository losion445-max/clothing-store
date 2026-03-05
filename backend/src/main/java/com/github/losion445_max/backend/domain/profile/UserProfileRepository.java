package com.github.losion445_max.backend.domain.profile;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository {

    Optional<UserProfile> findById(UUID id);

    void save(UserProfile profile);

    boolean existsById(UUID id);
    
}
