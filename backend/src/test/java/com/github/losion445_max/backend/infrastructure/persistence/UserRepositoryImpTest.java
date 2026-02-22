package com.github.losion445_max.backend.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.losion445_max.backend.domain.user.model.User;
import com.github.losion445_max.backend.domain.user.repository.UserRepository;

@Tag("integration")
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryImpTest {
    
    @Autowired
    private UserRepository repository;

    @Test
    void shouldSaveAndFindUserByIdAndEmail() {
        User user = User.create(
            "Name",
         "email@gmail.com",
          "hashPassword"
        );

        User savedUser = repository.save(user);
        assertNotNull(savedUser.getId());

        Optional<User> userById = repository.findById(savedUser.getId());
        assertTrue(userById.isPresent());
        assertEquals("Name", userById.get().getName());

        Optional<User> userByEmail = repository.findByEmail("email@gmail.com");
        assertTrue(userByEmail.isPresent());
        assertEquals("email@gmail.com", userByEmail.get().getEmail());
        
        assertTrue(repository.existsByEmail("email@gmail.com"));
        assertFalse(repository.existsByEmail("mail@mail.com"));
        
    }
}
