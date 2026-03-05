package com.github.losion445_max.backend.infrastructure.persistence.profile;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.github.losion445_max.backend.domain.profile.Address;
import com.github.losion445_max.backend.domain.profile.Phone;
import com.github.losion445_max.backend.domain.profile.UserProfile;


@Mapper(
    componentModel = "spring",
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface UserProfileMapper {

    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "mapToPhone")
    @Mapping(target = "isMarketingAllowed", expression = "java(entity.isMarketingAllowed())")
    UserProfile toDomain(UserProfileJpaEntity entity);

    @Mapping(target = "phoneNumber", source = "phoneNumber.value")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "isMarketingAllowed", expression = "java(userProfile.isMarketingAllowed())")
    UserProfileJpaEntity toEntity(UserProfile userProfile);

    @Mapping(target = "isPrimary", source = "primary")
    @Mapping(target = "withPrimary", ignore = true)
    Address toAddress(AddressEmbeddable embeddable);

    @Mapping(target = "primary", source = "isPrimary")
    AddressEmbeddable toEmbeddable(Address address);

    @Named("mapToPhone")
    default Phone mapToPhone(String phoneNumber) {
        return phoneNumber != null ? new Phone(phoneNumber) : null;
    }
}
