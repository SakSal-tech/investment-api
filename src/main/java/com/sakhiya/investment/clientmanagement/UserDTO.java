package com.sakhiya.investment.clientmanagement;

/**
 * DTO for user login and registration requests.
 * Only includes fields needed for authentication or registration.
 */
public class UserDTO {
    private String username;
    private String rawPassword;

    public UserDTO() {}

    public UserDTO(String username, String rawPassword) {
        this.username = username;
        this.rawPassword = rawPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }
}