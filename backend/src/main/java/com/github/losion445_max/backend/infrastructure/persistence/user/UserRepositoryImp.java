package com.github.losion445_max.backend.infrastructure.persistence.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.github.losion445_max.backend.domain.user.model.User;
import com.github.losion445_max.backend.domain.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserRepositoryImp implements UserRepository {

    private SpringDataUserRepository springRepository;
    private UserMapper userMapper;

    @Override
    public Optional<User> findById(UUID id) {
        return springRepository
        .findById(id)
        .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springRepository
        .findByEmail(email)
        .map(userMapper::toDomain);
    }
    
    @Override
    public User save(User user) {
        UserJpaEntity entity = userMapper.toEntity(user);
        UserJpaEntity savedEntity = springRepository.save(entity);

        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return springRepository.existsByEmail(email);
    }

}
