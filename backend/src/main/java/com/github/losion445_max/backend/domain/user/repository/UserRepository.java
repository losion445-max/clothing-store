package com.github.losion445_max.backend.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import com.github.losion445_max.backend.domain.user.model.User;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    User save(User user);
    Boolean existsByEmail(String email);
}
