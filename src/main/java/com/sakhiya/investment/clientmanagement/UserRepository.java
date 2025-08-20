
package com.sakhiya.investment.clientmanagement;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

/*This is an interface that extends ListCrudRepository<User, UUID>.
Spring Data JPA automatically provides the implementation at runtime.
It allows you to perform CRUD operations on the User table without writing SQL.
 */
public interface UserRepository extends ListCrudRepository<User, String> {
    // Find a client by username, username or username/email (from User class). This
    // will be linked and used in service class
    Optional<User> findByUsername(String Username);

    Optional<User> findByEmail(String email);

    // choice of the user could be found 
    Optional<User> findByUsernameOrEmail(String username, String email);

    // Security / Validation Queries
    // Check if a username is already taken
    Boolean existsByUsername(String username);

    // Check if an email is already registered
    Boolean existsByEmail(String email);

    // Verify if a client exists for login purposes
    Boolean existsByClient_ClientId(String clientId);

    // Returns activate users
    List<User> findByIsActive(boolean isActive);
    // Password reset token lookup
    //When a user requests a password reset, 
    // program generates a unique token and stores it in the user's record
    Optional<User> findByResetToken(String resetToken);

}
