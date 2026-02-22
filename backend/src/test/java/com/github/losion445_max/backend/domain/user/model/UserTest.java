package com.github.losion445_max.backend.domain.user.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
public class UserTest {
    
    @Test
    void testCreateUserHappyPath() {
        User user = User.create(
            "Name",
            "email@gmail.com",
            "hashPassword"
        );

        assertEquals("Name", user.getName());
        assertEquals("email@gmail.com", user.getEmail());
        assertEquals("hashPassword", user.getHashPassword());
        assertEquals(User.Role.USER, user.getRole());

        assertNull(user.getId());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    void testCreateUserEmptyName() {
        Exception exc = assertThrows(
            IllegalArgumentException.class, 
            () -> User.create("", "email@gmail.com", "hashPassword"));
        assertEquals("Name is required", exc.getMessage());
    }

    @Test
    void testCreateUserNullName() {
        Exception exc = assertThrows(
            IllegalArgumentException.class, 
            () -> User.create(null, "email@gmail.com", "hashPassword"));
        assertEquals("Name is required", exc.getMessage());
    }

    @Test
    void testCreateUserInvalidEmail() {
        Exception exc = assertThrows(
            IllegalArgumentException.class, 
            () -> User.create("Name", "bad-email", "hashPassword"));
        assertEquals("Email is invalid", exc.getMessage());
    }

    @Test
    void testCreateUserEmptyPassword() {
        Exception exc = assertThrows(
            IllegalArgumentException.class, 
            () -> User.create("Name", "email@gmail.com", ""));
        assertEquals("Password is required", exc.getMessage());
    }

    @Test
    void testCreateUserNullPassword() {
        Exception exc = assertThrows(
            IllegalArgumentException.class, 
            () -> User.create("Name", "email@gmail.com", null));
        assertEquals("Password is required", exc.getMessage());
    }
    
}
