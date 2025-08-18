package com.sakhiya.investment.clientmanagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
// annotation is used to map web requests onto specific handler classes and
// methods.
@RequestMapping("/api/clients")
public class ClientController {
    // coupling with ClientService. The controller user ClientService
    private final ClientService clientService;

    // Inject ClientService via the constructor.
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping(
            // Use @PathVariable when the value is part of the URL path (e.g.,
            // /api/clients/{clientId}).
            path = "/{clientId}", produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })

    public Client getClient(@PathVariable("clientId") UUID clientId) {
        // I could declare the parameter clientID as UUID or convert the parameter to
        // UUID as the method
        // UUID uuid = UUID.fromString(clientId) ;

        return clientService.getClient(clientId);
    }

    // Get clients by email server
    // I added /search/ for filtering or searching resources by fields that are not
    // the primary key (like postcode, email, etc.).

    @GetMapping("/search/email")
    public List<Client> getClientsByEmailServer(@RequestParam String emailServer) {
        return clientService.getClientsByEmailServer(emailServer);
    }

    // Get clients by postcode
    @GetMapping("/search/postcode")
    // RequestParam for query parameters (e.g.,
    // /api/clients/search/postcode?postcode=SW1A1AA)
    public List<Client> getClientsByPostcode(@RequestParam String postcode) {
        return clientService.getClientsByPostcode(postcode);
    }

    // Get clients by registration date range
    @GetMapping("/search/dates")
    public List<Client> getClientsByDates(
            // Spring can automatically convert String query parameters to LocalDate,
            // but I want to explicitly specify the format to avoid issues e.g YYYY-MM-DD
            // 2025-08-18
            // Without this, a format mismatch might cause a 400 error.
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return clientService.getClientsByDates(startDate, endDate);
    }

    // Get clients by active status
    @GetMapping("/search/active")
    public List<Client> getActiveClients(@RequestParam boolean isActive) {
        return clientService.getActiveClients(isActive);
    }

    // Get total client count
    @GetMapping("/count")
    public Long getCountClients() {
        return clientService.getCountClients();
    }

    // Get active client count
    @GetMapping("/count/active")
    public Long getCountActiveClients() {
        return clientService.getCountActiveClients();
    }

    // Get latest clients (limit)
    @GetMapping("/latest")
    public List<Client> getLatestClients(@RequestParam int limit) {
        return clientService.getLatestClients(limit);
    }

    @PostMapping(
            // consumes: What kind of request data (Content-Type) the server can accept.
            // produces: What kind of response data (Accept) the server will send

            consumes = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }, produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client savedClient = clientService.createClient(client);
        // ResponseEntity is a class provided by the Spring Framework that represents
        // the entire HTTP response, including the status code, headers, and body.
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{clientId}", consumes = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    }, produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public Client updateClient(@PathVariable("clientId") UUID clientId, @RequestBody Client updatedclient) {

        return clientService.updateClient(clientId, updatedclient);
    }

    

    @DeleteMapping(path = "/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable("clientId") UUID clientId) {
        clientService.deleteByClient(clientId);
        return ResponseEntity.noContent().build();
    }

    // exceptions like NoSuchElementException or IllegalArgumentException thrown by
    // ClientService will propagate as 500 Internal Server Error by default
    // handling them and return proper HTTP status codes
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNotFound(NoSuchElementException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(IllegalArgumentException e) {
        return e.getMessage();
    }

}