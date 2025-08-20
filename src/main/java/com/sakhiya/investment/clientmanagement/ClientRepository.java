package com.sakhiya.investment.clientmanagement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface ClientRepository extends ListCrudRepository<Client, String> {
    // List<Client> findAllClients(); // Removed redundant method, use findAll() from ListCrudRepository
  // Search Queries
    //Had to change to optional due to error in the ClientService update method The error occurs because 
    //calling orElseThrow directly on a Client object, but orElseThrow is a method of Optional, not Client. 
  Optional<Client> findByClientId(String clientId);
    // Find a client by username (from User class)
    // Optional means in case this returns null and surname does not exists
    Optional<Client> findOneBySurname(String surname);

      // Get all clients by postcode
  List<Client> findByPostCodeStartingWith(String postArea);

    // Delete a client by ID
    void deleteByClientId(String clientId);

    // Search clients by partial email match (e.g., contains “gmail”)
    List<Client> findByEmailContaining(String emailServer);

    // Find clients created within a date range
    List<Client> findByCreatedAtBetween(LocalDate start, LocalDate end);


    // Get clients by status (active, inactive)
    List<Client> findByActive(Boolean isActive);

    // Aggregation queries
    // Count total number of clients
    @Query("SELECT COUNT(c) FROM Client c")
    //After count all clients, return a long value
    long countAllClients();

    /**
     * Counts the number of clients whose 'active' field is set to true.
     * @return the number of active clients
     */
    @Query("SELECT COUNT(c) FROM Client c WHERE c.active = true")
    long countActiveClients();


    /*Pageable is a Spring Data JPA interface used for pagination and sorting. It allows  to:
      Limit the number of results returned (like SQL’s LIMIT) 
       iSSUE I HAD, JPQL does not support LIMIT 10.i HAD TO Use Pageable for limiting results in Spring Data JPA.
   */
    // Get the latest registered clients (e.g., last 10)
  @Query("SELECT c FROM Client c ORDER BY c.createdAt DESC")
    List<Client> findByLatestTenCreated(Pageable pageable);

}
