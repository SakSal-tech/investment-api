package com.sakhiya.investment.clientmanagement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
// ...existing code...

@Entity
@Table(name = "client")
public class Client {

    @Id
    @Column(name = "client_id", columnDefinition = "CHAR(36)")
    private String clientId = UUID.randomUUID().toString();  // store UUID as String
    //generates a proper 36-char string that maps to CHAR(36) in MySQL. 
    //I had issues with the way UUID is stored and the way is presented by postman problem how with JPA and Hibernate was mapping it 

    @NotNull
    private String firstName;

    @NotNull
    private String surname;

    @NotNull
    private LocalDate dob;

    private LocalDate createdAt;

    private String address;

    @NotNull
    private String postCode;
    private String telephone;

    @NotNull
    private String email;

    private boolean active;

    private String nationalInsuranceNumber;

    // JPA requires a public or protected no-args constructor
    public Client() {}

    // getters and setters
    public String getClientId() { return clientId; }
    public void setClientId(UUID clientId) { this.clientId = clientId.toString(); }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPostCode() { return postCode; }
    public void setPostCode(String postCode) { this.postCode = postCode; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean getActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getNationalInsuranceNumber() { return nationalInsuranceNumber; }
    public void setNationalInsuranceNumber(String nationalInsuranceNumber) { this.nationalInsuranceNumber = nationalInsuranceNumber; }
}
