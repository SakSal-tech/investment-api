package com.sakhiya.investment.clientmanagement;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

// ClientService is a business logic layer 
//it uses the repository to perform higher-level operations (like validation, exceptions, rules).

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients;
    }

        public Client getClient(UUID clientID) throws NoSuchElementException{
        // I had an error with int type. the id should be UUID type not int
        return clientRepository.findById(clientID)
            //orElseThrow Optional<T> is a container object in java.util that may or may not hold a non-null value
            .orElseThrow(() -> new NoSuchElementException("Client with this id: " + clientID + " is not found"));
    }


    public List<Client> getClientsByEmailServer(String emailServer) {
        if (emailServer == null || emailServer.isBlank()) {
            throw new IllegalArgumentException("Email server must not be null or blank");
        }
        return clientRepository.findByEmailContaining(emailServer);
    }


    public List<Client> getClientsByPostcode(String postcode) {
        //  separate parts of postcode by space
        String[] postArea = postcode.split("");
        //extract first part of the postcode which is index
        String area = postArea[0];
        if (postcode == null || postcode.isBlank()) {
            throw new IllegalArgumentException("Postcode must not be null or blank");
        }
    return clientRepository.findByPostCodeStartingWith(area);
    }

    public List<Client> getClientsByDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }
        return clientRepository.findByCreatedAtBetween(startDate, endDate);
    }

    public List<Client> getActiveClients(boolean isActive) {
        return clientRepository.findByActive(isActive);
    }
    public Long getCountClients() {
        return clientRepository.countAllClients();
    }

    public Long getCountActiveClients() {
        return clientRepository.countActiveClients();
    }

    public List<Client> getLatestClients(int limit){
            //PageRequest is a helper in Spring that lets you ask for just a part of a big list from the database, not the whole list at once using a limit with start page and maximum page. 
            //returns slice" of results called(a page)
            int max = 10;
            if(limit <=0 || limit > max){
                throw new IllegalArgumentException("Limit must be greater than zero and less than 10");
            }
            Pageable pageable = PageRequest.of(0, limit);
            return clientRepository.findByLatestTenCreated(pageable);
        }

    // OptimisticLockingFailureException It helps prevent data loss or conflicts
    // when multiple users/processes try to update the same data at the same time
    public Client createClient(Client client) throws IllegalArgumentException, OptimisticLockingFailureException {
        if (client.getFirstName() == null || client.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First Name cannot be null");

        }
        if (client.getSurname() == null || client.getSurname().isBlank()) {
            throw new IllegalArgumentException("Surname cannot be null");

        }
        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");

        }
        if (client.getAddress() == null || client.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or blank");

        }
        if (client.getPostCode() == null || client.getPostCode().isBlank()) {
            throw new IllegalArgumentException("Postcode cannot be null or blank");

        }

        return clientRepository.save(client);
    }

    // findByClientId returns an Optional<Client>. Optional is a container object
    // that may or may not contain a non-null value (in this case, a Client).
    // .orElseThrow is a concise way to handle the case where the client might not
    // exist.
    public Client updateClient(UUID clientId, Client updatedClient) throws NoSuchElementException {
        Client currentClient = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client with this id:" + clientId + " is not found"));
        // update database fiel(ds with new values: This is the new version of the Client —
        // usually passed in via the request body from the client (e.g. via JSON in a
        // PUT request).
        currentClient.setFirstName(updatedClient.getFirstName());
        currentClient.setSurname(updatedClient.getSurname());
        currentClient.setEmail(updatedClient.getEmail());
        currentClient.setAddress(updatedClient.getAddress());
        currentClient.setPostCode(updatedClient.getPostCode());
        // save the changes to the database
        return clientRepository.save(currentClient);
    }

    public void deleteByClient(UUID clientId) {
        // existsById is lighter query than isPresent() as it checks only if client id
        // exists rather than checking the whole record.
        if (clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
        } else {
            throw new NoSuchElementException("Client with this id:" + clientId + " is not found");
        }
    }

}
