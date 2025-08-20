package com.sakhiya.investment.clientmanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

// JUnit Jupiter (JUnit 5) annotations for test lifecycle and test methods
import org.junit.jupiter.api.BeforeEach; // Runs setup code before each test
import org.junit.jupiter.api.DisplayName; // Allows custom names for test classes/methods
import org.junit.jupiter.api.Test; // Marks a method as a test

// using Mockito is a mocking framework that allows you to create fake (mock) objects for dependencies,
// so I can test service in isolation without needing a real database or encoder(creating a mock object of BCryptPasswordEncoder to have control what happens when methods like encoder.encode() or encoder.matches() are called in  tests).

// Mockito annotations and utilities for mocking dependencies
import org.mockito.InjectMocks; // Automatically injects mocks into the tested class
import org.mockito.Mock; // Marks a field as a mock object
import org.mockito.MockitoAnnotations; // Initializes mock objects before tests

// Spring Security encoder (mocked here, not used directly in test)
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// DisplayName labels the test with a name you define clear, human-readable descriptions
@DisplayName(value = "User Service Test")

public class UserServiceTest {
    @Mock // Tells Mockito to create a mock UserRepository. Mockito will create a fake
          // version of UserRepository for your test.
    // To test service without needing a real database
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;// Tells Mockito to create a mock BCryptPasswordEncoder

    @InjectMocks // Tells Mockito to inject the above mocks into this UserService instance
    private UserService userService;

    @BeforeEach // Runs before each test method
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes the mocks and injects them
    }

    @Test //// Marks this as a test method for JUnit
    @DisplayName("Returns user if user exists, throws if not ")
    void checkUserExist() {

        // Create a user object(requires the no args constructor in the user class)
        User user = new User();
        String username = "sakhiya.s";

        user.setUsername(username);
        // user.setEmail("sakhiya@yaho.com");
        // user.setUserId("995c2d4c-7c82-11f0-a429-84ba590b0166");

        // Mock the repository to return the user when searched by username
        // I tried if statement and it did not work. when is a special Mockito method
        // used to define what a mock should do when a method is called.
        /*
         * https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/module-
         * summary.html
         * https://www.baeldung.com/mockito-behavior
         * https://docs.google.com/document/d/15mJ2Qrldx-
         * J14ubTEnBj7nYN2FB8ap7xOn8GRAi24_A/edit?tab=t.0
         */
        // when user exist
        // calls userRepository.findByUsername(username), return an Optional(becasue
        // this method is optional in the repository) containing the user object
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        // This calls service method to look up a user by username
        User found = userService.getUserByUserName(username);
        String foundUsername = found.getUsername();// extract username from the object user returned
        // checks that the found username is not equal to the username string
        assertEquals(username, foundUsername);// Assert the username matches

        // when user does not exist If called userRepository.findByUsername("not
        // found"), return an empty Optional (meaning no user found).”
        when(userRepository.findByUsername("not found")).thenReturn(Optional.empty());
        // Testing error handling. checking that, when getUserByUserName is called with
        // a username that does not exist, the method actually throws a
        // NoSuchElementException.
        // assertThrows is a JUnit assertion that expects the code inside it to throw
        // the specified exception
        assertThrows(NoSuchElementException.class, () -> userService.getUserByUserName("not found"));

    }

    @Test
    @DisplayName("Throws exception if username already exists when creating user")
    void createUserThrowsIfUsernameExists() {
        User user = new User();
        user.setUsername("sakhiya.s");
        user.setEmail("test@example.com");
        user.setRawPassword("password123");

        // Simulate that the username already exists in the repository
        when(userRepository.findByUsername("sakhiya.s")).thenReturn(Optional.of(new User()));

        // This should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    // Test for Blank Password
    @Test
    void createUserThrowsWhenPasswordBlank() {
        User user = new User();
        user.setUsername("newUser");
        user.setEmail("test@example.com");
        user.setRawPassword(""); // Blank password

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

}
