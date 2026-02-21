package com.github.losion445_max.backend.infrastructure.persistence.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.github.losion445_max.backend.domain.user.model.User;
import com.github.losion445_max.backend.domain.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@AllArgsConstructor
public class UserRepositoryImp implements UserRepository {

    private SpringDataUserRepository springRepository;
    private UserMapper userMapper;


    @Override
    public Optional<User> findById(UUID id) {
        log.debug("Looking for use with id={}", id);

        Optional<User> userOpt =  springRepository
        .findById(id)
        .map(userMapper::toDomain);
         
        if (userOpt.isPresent()) {
            log.info("User found with id={}", userOpt.get().getId());
        } else {
            log.warn("User not found with id={}", id);
        }

        return userOpt;
    }


    @Override
    public Optional<User> findByEmail(String email) {
        log.debug("Looking for user with email={}", email);

        Optional<User> userOpt = springRepository
        .findByEmail(email)
        .map(userMapper::toDomain);

        if (userOpt.isPresent()) {
            log.info("User found with email={}", userOpt.get().getEmail());
        } else {
            log.warn("User not found with email={}", email);
        }

        return userOpt;


    }
    
    @Override
    public User save(User user) {
        log.debug("Saving user with email={}", user.getEmail());
        
        try {
            UserJpaEntity entity = userMapper.toEntity(user);
            UserJpaEntity savedEntity = springRepository.save(entity);

            log.info("User saved successfully id={}", savedEntity.getId());
            return userMapper.toDomain(savedEntity);
        } catch (Exception exc) {
            log.error("Failed to save user email={}", user.getEmail(), exc);
            throw exc;
        } 

    }

    @Override
    public Boolean existsByEmail(String email) {
        log.debug("Check if email={} exists", email);
        boolean exists = springRepository.existsByEmail(email);
        log.info("User with email {} exists? {}", email, exists);

        return exists;
    }

}
