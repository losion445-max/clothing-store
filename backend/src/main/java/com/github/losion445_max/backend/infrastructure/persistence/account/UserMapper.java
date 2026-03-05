package com.github.losion445_max.backend.infrastructure.persistence.account;

import org.mapstruct.Mapper;

import com.github.losion445_max.backend.domain.account.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDomain(UserJpaEntity entity);
    UserJpaEntity toEntity(User user);
}
