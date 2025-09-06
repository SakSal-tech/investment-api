package com.sakhiya.investment.clientmanagement;

import org.springframework.stereotype.Service;
// importing the Validations class to use isValidEmail method.
import com.sakhiya.investment.util.Validations;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;// BCrypt is a secure, industry-standard hashing algorithm that Includes a salt automatically (protects against rainbow table attacks). 
//Is computationally slow enough to make brute force attacks harder deliberately

/*This is a service class, annotated with @Service.
It contains business logic and uses UserRepository to interact with the database.
It is injected with UserRepository (via constructor but could use @Autowired if there were more constructors). */

//Indicates that an annotated class is a Service class 
@Service
public class UserService {

    // Inject the UserRepository dependency
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    // Removed users field to avoid stale data issues

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUserName(String userName) throws NoSuchElementException {
        return userRepository.findByUsername(userName)
                // orElseThrow Optional<T> is a container object in java.util that may or may
                // not hold a non-null value
                .orElseThrow(
                        () -> new NoSuchElementException("User with this userName: " + userName + " is not found"));
    }

    public User getUserByEmail(String email) throws NoSuchElementException {
        return userRepository.findByEmail(email)
                // orElseThrow Optional<T> is a container object in java.util that may or may
                // not hold a non-null value
                .orElseThrow(() -> new NoSuchElementException("User with this email: " + email + " is not found"));
    }

    /**
     * This method accepts 2 parameters but either one is accepted. Users could be
     * found by username or email.
     */
    public User getUserByEmailOrUsername(String userName, String email) throws NoSuchElementException {
        // validation if either email or userName only is inputted to call other methods
        // findByUsername and findByEmail
        if (userName != null && !userName.isBlank()) {
            return userRepository.findByUsername(userName)
                    .orElseThrow(
                            () -> new NoSuchElementException("User with this username: " + userName + " is not found"));
        } else if (email != null && !email.isBlank()) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new NoSuchElementException("User with this email: " + email + " is not found"));
        } else {
            throw new IllegalArgumentException("Either username or Email must be inputted");
        }
    }

    public List<User> getActiveUsers() {
        return userRepository.findByIsActive(true); // or whatever your repository method is
    }

    /**
     * Attempts to authenticate a user by username and password.
     * If the user is inactive or locked out, this method does not allow login and
     * throws an exception.
     * Throws NoSuchElementException if the user is not found or credentials are
     * invalid.
     * Note: Handling for locked out users is not implemented here; only active
     * users can log in.
     *
     * @param userName    the username of the user
     * @param rawPassword the raw (plain text) password
     * @return the authenticated User if credentials are valid and user is active
     * @throws NoSuchElementException if authentication fails
     */
    public User userLogin(String userName, String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("Password must not be null");
        }

        // Look up the user by username (using the repository)

        User user = userRepository.findByUsername(userName)
                .orElseThrow(
                        () -> new NoSuchElementException("User with this username: " + userName + " is not found"));
        // encoder.matches is a method from BCryptPasswordEncoder that checks if a raw
        // (plain text) password matches a previously hashed password.
        if (encoder.matches(rawPassword, user.getPasswordHash())) {
            return user; // login successful
        }
        // If password does not match, handle failed login attempts (not implemented
        // here)
        throw new NoSuchElementException("Invalid credentials. Reset your password.");
    }

    // This method generates a unique, random token (usually a UUID) and stores it
    // in the user's record.
    // It is used when a user requests a password reset (e.g., “Forgot Password?”).
    // In future improved system, the plan is that token is sent to the user
    // (usually via email) as a link to verify their identity and allow them to
    // reset their password.
    public User generateResetToken(String userName) {
        // find user to to generate a reset token by creating a user object and calling
        // the generate method
        User user = userRepository.findByUsername(userName).orElseThrow(
                () -> new NoSuchElementException("User with this username: " + userName + " is not found"));
        user.generateResetToken();
        // To save)the new token value to the database field, calling save on the
        // repository
        // Save and return the updated user entity
        return userRepository.save(user);

    }

    /**
     * Looks up a user by their password reset token.
     * Throws IllegalArgumentException if the token is null or blank.
     * Throws NoSuchElementException if no user is found for the given token.
     * Used during password reset flows to validate and retrieve the user associated
     * with a reset token.
     */
    public User findByResetToken(String resetToken) {
        System.out.println("Looking for user with resetToken: " + resetToken);

        if (resetToken == null || resetToken.isBlank()) {
            throw new IllegalArgumentException("resetToken must not be null or blank");
        }
        // because findByResetToken returns optional, if there is no user throw
        return userRepository.findByResetToken(resetToken)
                .orElseThrow(() -> new NoSuchElementException("No user found for reset token: " + resetToken));
    }

    public User resetPassword(String userName, String newPassword) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(
                        () -> new NoSuchElementException("User with this username: " + userName + " is not found"));
        user.setPasswordHash(encoder.encode(newPassword));//The password is updated for the user with the given username
        user.setResetToken(null); // Clear the reset token after use
        userRepository.save(user);
        return user;
    }

    public Boolean validatePassword(String password) {
        // return ;

        /*
         * Regex checking Has minimum 8 characters in length * {8,}
         * At least one uppercase English letter (?=.*?[A-Z]). ?=.*? means look ahead
         * and check the presence of characters
         * At least one lowercase English letter. (?=.*?[a-z])
         * At least one digit (?=.*?[0-9])
         * At least one special character (?=.*?[#?!@$%^&*-])
         * . means "any single character except a newline
         * Pattern.compile(...) creates a compiled regular expression pattern from the
         * string you provide.
         * .matcher(password) creates a Matcher object that will check if the password
         * matches the pattern.
         * .find() checks if there is any part of the password that matches the pattern
         * (in your case, it checks the whole string because of the ^ and $ anchors)
         */

        boolean isValidPassword = password.length() >= 8 && password.length() <= 15
                && Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-])$")
                        .matcher(password).find();
        return isValidPassword;

    }

    // Use Validations.isValidEmail(email) from com.sakhiya.investment.util.Validations for email validation.

    /**
     * 
     * Creates a new user. Throws IllegalArgumentException if username or email
     * already exists.
     */
    public User createUser(User user) {
        // Check for existing username or email
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        // Validate email format
        if (!Validations.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        // Hash the raw password before saving
        if (user.getRawPassword() == null || user.getRawPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be null or blank");
        }
        user.setPasswordHash(encoder.encode(user.getRawPassword()));
        userRepository.save(user);
        return user;
    }

    /**
     * Updates an existing user by username.
     */
    public User updateUser(String username, User updatedUser) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new NoSuchElementException("User with this username: " + username + " is not found"));
        // Update email if provided and different
        String newEmail = updatedUser.getEmail();
        if (newEmail != null && !newEmail.equals(existingUser.getEmail())) {
            if (!Validations.isValidEmail(newEmail)) {
                throw new IllegalArgumentException("Invalid email format");
            }
            existingUser.setEmail(newEmail);
        }
        // Update password if provided
        if (updatedUser.getRawPassword() != null && !updatedUser.getRawPassword().isBlank()) {
            existingUser.setPasswordHash(encoder.encode(updatedUser.getRawPassword()));
        }
        // Update isActive if provided
        if (updatedUser.getIsActive() != null) {
            existingUser.setIsActive(updatedUser.getIsActive());
        }
        userRepository.save(existingUser);
        return existingUser;
    }

    /**
     * Deletes a user by username.
     */
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new NoSuchElementException("User with this username: " + username + " is not found"));
        userRepository.delete(user);
    }

}
