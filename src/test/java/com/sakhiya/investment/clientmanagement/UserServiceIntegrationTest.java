package com.sakhiya.investment.clientmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;

@SpringBootTest
@Transactional // Rolls back changes after each test All changes made to the database during the test are rolled back (undone) after the test finishes

public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    /*
    @Test
    // deliberately passing arguments of existing username for test to fail when a
    // duplicate username
    @DisplayName("Creates a user if user does not exists, throws if not ")

    void testFindUserExist() {
        User user = new User();
        user.setUsername("sakhiya.s");
        user.setEmail("sakhiya@example.com");
        user.setRawPassword("password123");

        userService.createUser(user);

        User found = userService.getUserByUserName("sakhiya.s");
        assertEquals("sakhiya.s", found.getUsername());
    } */

    @Test
    @DisplayName("Creates a user if username and email do not exist")
    void testCreateAndFindUser() {
        // Generate a unique username and email for each test run
        String username = "integrationUser" + System.currentTimeMillis();
        String email = username + "@example.com";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRawPassword("password123");

        userService.createUser(user);

        User found = userService.getUserByUserName(username);
        assertEquals(username, found.getUsername());
        assertEquals(email, found.getEmail());
    }

    @Test
@DisplayName("userLogin works for an existing user in the real database")
void testUserLoginWithExistingUser() {
    String username = "existingUsername"; // Use a real username from your database
    String password = "actualPassword";   // Use the real (raw) password for that user

    User loggedIn = userService.userLogin(username, password);
    assertNotNull(loggedIn);
    assertEquals(username, loggedIn.getUsername());
}

@Test
@DisplayName("userLogin throws if user does not exist in the real database")
void testUserLoginWithNonExistentUser() {
    String username = "thisUserDoesNotExist12345"; // Use a username you know does NOT exist
    String password = "anyPassword";

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
    assertThrows(NoSuchElementException.class, () -> userService.userLogin("existingUsername", ""));
}

@Test
@DisplayName("userLogin throws if password is null")
void testUserLoginNullPassword() {
    assertThrows(NoSuchElementException.class, () -> userService.userLogin("existingUsername", null));
}

}
