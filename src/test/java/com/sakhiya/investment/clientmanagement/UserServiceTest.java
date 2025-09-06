package com.sakhiya.investment.clientmanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DisplayName("User Service Test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    // Test that a user is returned when the username exists in the repository
    @Test
    @DisplayName("Returns user if user exists")
    void userExists() {
        User user = new User();
        String username = "sakhiya.s";
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User found = userService.getUserByUserName(username);

        assertEquals(username, found.getUsername());
    }

    // Test that a NoSuchElementException is thrown when username does not exist
    @Test
    @DisplayName("Throws exception if user does not exist")
    void userDoesNotExist() {
        when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserByUserName("notfound"));
    }

    // Test that creating a user fails if the username already exists
    @Test
    @DisplayName("Throws exception if username already exists when creating user")
    void createUserThrowsIfUsernameExists() {
        User user = new User();
        user.setUsername("sakhiya.s");
        user.setEmail("test@example.com");
        user.setRawPassword("password123");

        when(userRepository.findByUsername("sakhiya.s")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    // Test that creating a user fails if the password is blank
    @Test
    @DisplayName("Throws exception if password is blank when creating user")
    void createUserThrowsWhenPasswordBlank() {
        User user = new User();
        user.setUsername("newUser");
        user.setEmail("test@example.com");
        user.setRawPassword(""); // Blank password

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    // Test that creating a user succeeds when username/email are unique and password is valid
    @Test
    @DisplayName("Creates user successfully when username and email are unique and password is valid")
    void createUserSucceedsWhenValid() {
        User user = new User();
        user.setUsername("uniqueUser");
        user.setEmail("unique@example.com");
        user.setRawPassword("validPass123");

        when(userRepository.findByUsername("uniqueUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("unique@example.com")).thenReturn(Optional.empty());
        when(encoder.encode(anyString())).thenReturn("encodedPass");

        User created = userService.createUser(user);

        assertNotNull(created); // Ensure user is created
        assertEquals("uniqueUser", created.getUsername());
        assertEquals("encodedPass", created.getPasswordHash()); // Ensure password is encoded
    }
}
