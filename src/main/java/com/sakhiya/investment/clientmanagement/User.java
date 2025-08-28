package com.sakhiya.investment.clientmanagement;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "`user`")
public class User {
    //@Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "userId", columnDefinition = "CHAR(36)")
    private String userId;


    // moved this to generateId
    //private String userId = UUID.randomUUID().toString();  // store UUID as String
    //generates a proper 36-char string that maps to CHAR(36) in MySQL. 
    //I had issues with the way UUID is stored and the way is presented by postman problem how with JPA and Hibernate was mapping it 

    
    private String username;
    private String passwordHash;
    @NotBlank
    @Email
    private String email;
    private Boolean isActive;
    private String resetToken;

    @PrePersist
    public void generateId() {
        if (this.userId == null) {
            this.userId = UUID.randomUUID().toString();
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @OneToOne // one client has one user acount
    @JoinColumn(name = "client_id", columnDefinition = "CHAR(36)") // joining clientId which is PK in Client table here as FK
    private Client client;

    // no args constructor for mock tests and JPA(Java Persistence API)
    public User() {}
    public User(String userId, String username, String passwordHash, String email, Client client) {
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
        this.passwordHash = passwordHash; // just assign, no encoding here
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

    public void generateResetToken() {
        // automatically generate a password reset token
        this.resetToken = UUID.randomUUID().toString();
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getUserId() {
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

    // Not persisted, only used for registration/update
    private String rawPassword;

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }
}
