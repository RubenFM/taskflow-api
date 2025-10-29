package com.taskflow.service;

import com.taskflow.exception.ResourceNotFoundException;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.model.User;
import com.taskflow.model.UserRole;
import com.taskflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.taskflow.model.UserRole.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        // When
        List<User> result = userService.getAllUsers();
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getName(), result.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldThrowException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(999L));

        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        User updatedData = new User();
        updatedData.setName("Updated user");
        updatedData.setRole(USER);
        updatedData.setEmail("user@email.com");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Old User");
        existingUser.setRole(UserRole.ADMIN);
        existingUser.setEmail("olduser@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updatedData);

        assertNotNull(result);
        assertEquals("Updated user", result.getName());
        assertEquals("user@email.com", result.getEmail());
        assertEquals(USER, result.getRole());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);

    }

    @Test
    void updateUser_WhenUserNotExists_ShouldThrowException() {
        User updatedData = new User();
        updatedData.setName("Updated user");
        updatedData.setRole(USER);
        updatedData.setEmail("user@email.com");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(999L, updatedData));

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldThrowException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(999L));

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).delete(any(User.class));
    }
}