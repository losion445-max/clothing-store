-- Liquibase formatted sql

-- changeset losion445:create_user_profiles_table
CREATE TABLE IF NOT EXISTS user_profiles (
    id UUID PRIMARY KEY,
    full_name VARCHAR(255),
    phone_number VARCHAR(20),
    birth_date DATE,

    is_marketing_allowed BOOLEAN NOT NULL DEFAULT FALSE,
    marketing_consent_updated_at TIMESTAMP WITH TIME ZONE,

    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP 
);

-- changeset losion445:create-user-address
CREATE TABLE IF NOT EXISTS user_addresses (
    id UUID PRIMARY KEY,
    user_profile_id UUID NOT NULL,
    label VARCHAR(50) NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    city VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL,
    street_line VARCHAR(255) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_user_profile
        FOREIGN KEY (user_profile_id)
        REFERENCES user_profiles (id)
        ON DELETE CASCADE
);

-- changeset losion445:add-indexes
CREATE INDEX idx_user_addresses_profile_id ON user_addresses(user_profile_id);
CREATE INDEX idx_user_profiles_phone ON user_profiles(phone_number) WHERE phone_number IS NOT NULL;
