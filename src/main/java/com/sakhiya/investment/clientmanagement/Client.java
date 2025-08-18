package com.sakhiya.investment.clientmanagement;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "client")

/**
 * Represents a client in the investment management system.
 * Stores personal and contact information for each client.
 */
public class Client {
    /**
     * The unique identifier for the client (primary key).
     */
    // both client_id fields are CHAR(36), to prevent mismatch.
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID clientId = UUID.randomUUID(); // generated in Java
    
    // JPA requires a public or protected no-args constructor

    public Client() {
    }

    @NotNull
    private String firstName;

    @NotNull
    private String surname;

    /**
     * The date of birth of the client.
     */
    @NotNull
    private LocalDate dob;

    private LocalDate createdAt;

    private String address;

    @NotNull
    private String postCode;
    private String telephone;

    @NotNull
    private String email;

    /**
     * Indicates if the client is currently active.
     */
    private boolean active;

    /**
     * The national insurance number of the client.
     */
    private String nationalInsuranceNumber;

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public boolean getActive() {
        return active;
    }

    public String getNationalInsuranceNumber() {
        return nationalInsuranceNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setNationalInsuranceNumber(String nationalInsuranceNumber) {
        this.nationalInsuranceNumber = nationalInsuranceNumber;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
}


}
