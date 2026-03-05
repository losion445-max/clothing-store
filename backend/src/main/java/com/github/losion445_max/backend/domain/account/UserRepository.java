package com.github.losion445_max.backend.domain.account;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    User save(User user);
    Boolean existsByEmail(String email);
}
