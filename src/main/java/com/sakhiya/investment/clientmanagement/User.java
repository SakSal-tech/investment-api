package com.sakhiya.investment.clientmanagement;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// remember to add dependency spring-boot-starter-validation to the POM file
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;// BCrypt is a secure, industry-standard hashing algorithm that Includes a salt automatically (protects against rainbow table attacks). 
//Is computationally slow enough to make brute force attacks harder deliberately
import org.springframework.security.crypto.password.PasswordEncoder; //An interface that defines the contract for encoding (hashing) passwords and verifying them. Allows swapping out different password hashing algorithms without changing your code logic


@Entity
@Table(name = "user")
public class User {// for normalisation and scalability reasons I moved user details to its table insted of fields in client tabls
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    private String username;
    private String passwordHash;
     @NotBlank
    @Email
    private String email;
    private Boolean isActive;
    private String resetToken;

    @OneToOne // one client has one user acount
   @JoinColumn(name = "client_id", columnDefinition = "CHAR(36)") // joining clientId which is PK in Client table here as FK
    private Client client;

        public User(UUID userId, String username, String passwordHash, String email, Client client) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.client = client;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        // password cannot be stored just as plain String. it has to be encrypted.
        PasswordEncoder encoder = new BCryptPasswordEncoder(); // BCryptPasswordEncoder is a class that implements the PasswordEncoder interface using the BCrypt hashing algorithm.
        this.passwordHash = encoder.encode(passwordHash); // hash password before saving
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setClient(Client client) {
        this.client = client;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

        public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public Client getClient() {
        return client;
    }

        public Boolean getIsActive() {
        return isActive;
    }


    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }



}
