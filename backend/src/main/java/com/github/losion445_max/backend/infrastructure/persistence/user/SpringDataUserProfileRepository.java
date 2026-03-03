package com.github.losion445_max.backend.infrastructure.persistence.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SpringDataUserProfileRepository  extends JpaRepository<UserProfileJpaEntity, UUID> {
    Optional<UserProfileJpaEntity> findById(UUID id);
    void save(UserJpaEntity profile);
    boolean existsById(UUID id);
}
