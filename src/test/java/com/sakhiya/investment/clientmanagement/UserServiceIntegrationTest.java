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
import com.sakhiya.investment.util.Validations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;

@SpringBootTest
@Transactional // Rolls back changes after each test. All changes made to the database during
               // the test are rolled back (undone) after the test finishes
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("checks password is valid and returns false if not")
    void testPasswordIsValid() {
        String shortPassword = "abcd"; // too short
        boolean isValid = userService.validatePassword(shortPassword);
        assertFalse(isValid); // Expect false because password is too short
    }

    @Test
    @DisplayName("checks email is valid and returns false if not")
    void testEmailIsValid() {
        String invalidEmail = "abcd@hotmail";
        // Refactored and moved email validations to the shared validations class as it is needed in both client and user classes
        boolean isValid = Validations.isValidEmail(invalidEmail);
        assertFalse(isValid); // Expect false because email format is invalid
    }

    @Test
    @DisplayName("Creates a user if username and email do not exist")
    void testCreateAndFindUser() {
        // Refactoring to follow SOLID principle: use helper for creating unique user
        User user = createUniqueUser("integrationUser");

        // Check that the user was successfully created
        User found = userService.getUserByUserName(user.getUsername());
        assertEquals(user.getUsername(), found.getUsername()); // username matches
        assertEquals(user.getEmail(), found.getEmail()); // email matches
    }

    @Test
    @DisplayName("userLogin works for a newly created user")
    void testUserLoginWithExistingUser() {
        // Create a unique user for this test using helper
        User user = createUniqueUser("integrationUser");

        User loggedIn = userService.userLogin(user.getUsername(), user.getRawPassword());
        assertNotNull(loggedIn); // user should exist
        assertEquals(user.getUsername(), loggedIn.getUsername());
    }

    @Test
    @DisplayName("userLogin throws if user does not exist in the real database")
    void testUserLoginWithNonExistentUser() {
        String username = "Ali.J"; // Using a username that does NOT exist
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
        // Use helper to create a valid user
        User user = createUniqueUser("blankPasswordUser");
        assertThrows(NoSuchElementException.class, () -> userService.userLogin(user.getUsername(), ""));
    }

    @Test
    @DisplayName("userLogin throws if password is null")
    void testUserLoginNullPassword() {
        // Use helper to create a valid user
        User user = createUniqueUser("nullPasswordUser");
        assertThrows(IllegalArgumentException.class, () -> userService.userLogin(user.getUsername(), null));
    }

    // Helper method to create a unique user for tests
    // Refactoring to follow SOLID principles (Single Responsibility & DRY):
    // - Each test no longer has to repeat user creation logic
    // - Test methods focus only on what they are actually testing.
    // Previously I had to call userService.createUser(user) multiple times
    private User createUniqueUser(String baseUsername) {
        String username = baseUsername + System.currentTimeMillis();
        String email = username + "@example.com";
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRawPassword("ValidPassword123");
        userService.createUser(user); // Actually creates the user in the database
        return user;
    }

    @Test
    @DisplayName("Creates user successfully when username and email are unique")
    void testCreateUserSuccess() {
        // Using helper to create a unique user
        User user = createUniqueUser("uniqueUser");

        // Fetch the user from the database to ensure it was created
        User found = userService.getUserByUserName(user.getUsername());

        // Assertions to verify correct creation
        assertEquals(user.getUsername(), found.getUsername(), "Username should match");
        assertEquals(user.getEmail(), found.getEmail(), "Email should match");
        assertNotNull(found.getRawPassword(), "Password should be set");
    }

}
