package com.github.losion445_max.backend.infrastructure.persistence.user;

import org.mapstruct.Mapper;

import com.github.losion445_max.backend.domain.user.model.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDomain(UserJpaEntity entity);
    UserJpaEntity toEntity(User user);
}
