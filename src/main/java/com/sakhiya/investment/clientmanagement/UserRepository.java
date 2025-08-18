
package com.sakhiya.investment.clientmanagement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository <User, UUID>{

    
    //Find a client by username, username or username/email (from User class). This will be linked and used in service class
    Optional <User> findByUsername(String Username);
    Optional <User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);

    
    //Security / Validation Queries
    //Check if a username is already taken
    Boolean existsByUsername(String username);


    //Check if an email is already registered
    Boolean existsByEmail(String email);

    //Verify if a client exists for login purposes 
    Boolean existsByClient_ClientId(UUID clientId);

    // Returns activate users 
    List<User> findByIsActive(Boolean isActive);

    //Password reset token lookup 
    Optional<User> findByResetToken(String resetToken);


 
 




}


