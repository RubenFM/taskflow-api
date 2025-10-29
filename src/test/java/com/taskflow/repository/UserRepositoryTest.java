package com.taskflow.repository;

import com.taskflow.model.User;
import com.taskflow.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRole(UserRole.USER);
    }

    @Test
    void save_ShouldPersistUser() {
        User result = userRepository.save(user);

        assertNotNull(result.getId());
        assertEquals("Test User", result.getName());
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        User savedUser = userRepository.save(user);

        Optional<User> result = userRepository.findById(savedUser.getId());

        assertTrue(result.isPresent());
        assertEquals("Test User", result.get().getName());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenUserNotExists() {
        Optional<User> result = userRepository.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@email.com");
        user2.setRole(UserRole.ADMIN);

        userRepository.save(user2);
        userRepository.save(user);
        List<User> result = userRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void delete_ShouldRemoveUser() {
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        userRepository.deleteById(userId);

        Optional<User> result = userRepository.findById(userId);

        assertFalse(result.isPresent());
        assertEquals(0, userRepository.findAll().size());
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("Test User", result.get().getName());
        assertEquals("test@example.com", result.get().getEmail());
    }
}