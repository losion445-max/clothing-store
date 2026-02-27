package com.github.losion445_max.backend.infrastructure.persistence.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID>{
    Optional<UserJpaEntity> findByEmail(String email);
    Boolean existsByEmail(String email);

}
