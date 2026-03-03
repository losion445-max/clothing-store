package com.github.losion445_max.backend.infrastructure.persistence.user;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileJpaEntity {
    @Id
    private UUID id;

    private String fullName;
    private String phoneNumber;
    private LocalDate birthDate;

    @ElementCollection
    @CollectionTable(
        name = "user_addresses",
        joinColumns = @JoinColumn(name = "user_profile_id")
    )
    @Builder.Default
    private List<AddressEmbeddable> addresses = new ArrayList<>();

    private boolean isMarketingAllowed;
    private Instant marketingConsentUpdatedAt;

    @Version
    private Long version;
}


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class AddressEmbeddable {
    private String label;
    private String countryCode;
    private String city;
    private String postalCode;
    private String streetLine;
    private boolean isPrimary;
}
