/**
 * Integration tests for the UserService class.
 * This class verifies user creation and login functionality,
 * including handling of invalid input and non-existent users.
 */
package com.sakhiya.investment.clientmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;

@SpringBootTest
@Transactional // Rolls back changes after each test All changes made to the database during
               // the test are rolled back (undone) after the test finishes

public class UserServiceIntegrationTest {

    @Test
    @DisplayName("checks password is valid and returns false if not")
    void testPasswordIsValid() {
        String shortPassword = "abcd"; // too short
        boolean isValid = userService.validatePassword(shortPassword);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("checks email is valid and returns false if not")
    void testEmailIsValid() {
        String invalidEmail = "abcd@hotmail"; 
        boolean isValid = userService.isValidEmail(invalidEmail);
        assertFalse(isValid);
    }
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Creates a user if username and email do not exist")
    void testCreateAndFindUser() {
        // Generate a unique username and email for each test run
        String username = "integrationUser" + System.currentTimeMillis();
        String email = username + "@example.com";
        String password = "password123";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRawPassword(password);

        userService.createUser(user);

        User found = userService.getUserByUserName(username);
        assertEquals(username, found.getUsername());
        assertEquals(email, found.getEmail());
    }

    @Test
    @DisplayName("userLogin works for a newly created user")
    void testUserLoginWithExistingUser() {
        // Create a unique user for this test
        String username = "integrationUser" + System.currentTimeMillis();
        String email = username + "@example.com";
        String password = "Password123!";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRawPassword(password);
        userService.createUser(user);

        User loggedIn = userService.userLogin(username,  password);
        assertNotNull(loggedIn);
        assertEquals(username, loggedIn.getUsername());
    }
    

    @Test
    @DisplayName("userLogin throws if user does not exist in the real database")
    void testUserLoginWithNonExistentUser() {
        String username = "Ali.J"; // Use a username you know does NOT exist
        String password = "Password123!";

        assertThrows(NoSuchElementException.class, () -> userService.userLogin(username, password));
    }

    @Test
    @DisplayName("userLogin throws if username is blank")
    void testUserLoginBlankUsername() {
        assertThrows(NoSuchElementException.class, () -> userService.userLogin("", "anyPassword"));
    }

    @Test
    @DisplayName("userLogin throws if username is null")
    void testUserLoginNullUsername() {
        assertThrows(NoSuchElementException.class, () -> userService.userLogin(null, "anyPassword"));
    }

    @Test
    @DisplayName("userLogin throws if password is blank")
    void testUserLoginBlankPassword() {
        // always create a unique user before each login attempt.
        // This ensures the user exists and avoids conflicts or missing user errors
        // Use a unique username for this test
        String username = "blankPasswordUser" + System.currentTimeMillis();
        String email = username + "@example.com";
        String validPassword = "ValidPassword123";
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRawPassword(validPassword);
        userService.createUser(user);
        assertThrows(NoSuchElementException.class, () -> userService.userLogin(username, ""));
    }

    @Test
    @DisplayName("userLogin throws if password is null")
    void testUserLoginNullPassword() {
        // always create a unique user before each login attempt.
        // This ensures the user exists and avoids conflicts or missing user errors
        // Use a unique username for this test

        String username = "nullPasswordUser" + System.currentTimeMillis();
        String email = username + "@example.com";
        String validPassword = "ValidPassword123";
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRawPassword(validPassword);
        userService.createUser(user);
        assertThrows(IllegalArgumentException.class, () -> userService.userLogin(username, null));
    }

}
