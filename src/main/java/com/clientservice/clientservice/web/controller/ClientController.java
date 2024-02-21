package com.clientservice.clientservice.web.controller;

import com.clientservice.clientservice.business.service.ClientService;
import com.clientservice.clientservice.model.Client;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.clientservice.clientservice.swagger.DescriptionVariables;

import java.util.List;
import java.util.Optional;

@Api(tags = {DescriptionVariables.CLIENT})
@Log4j2
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    @ApiOperation(value = "Finds all clients",
            notes = "Returns the entire list of clients",
            response = Client.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded", response = Client.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<List<Client>> findAllClients() {
        log.info("Retrieve list of Clients");
        List<Client> clientList = clientService.findAllClients();
        if (clientList.isEmpty()) log.warn("Client list is empty! {}", clientList);
        else log.debug("Client list is found. Size: {}", clientList::size);
        return ResponseEntity.ok(clientList);
    }


    @GetMapping("/active")
    @ApiOperation(value = "Finds all active clients",
           notes = "Returns the entire list of active clients",
           response = Client.class, responseContainer = "List")
    @ApiResponses(value = {
           @ApiResponse(code = 200, message = "The request has succeeded", response = Client.class, responseContainer = "List"),
           @ApiResponse(code = 401, message = "The request requires user authentication"),
           @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
           @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
           @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<List<Client>> findAllActiveClients() {
       log.info("Retrieve list of active clients");
       List<Client> clientList = clientService.findAllActiveClients();
       if (clientList.isEmpty()) log.warn("Active client list is empty! {}", clientList);
       else log.debug("Active client list is found. Size: {}", clientList::size);
       return ResponseEntity.ok(clientList);
    }



    @GetMapping("/{id}")
    @ApiOperation(value = "Find the client by id",
            notes = "Provide an id to search specific client in database",
            response = Client.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<Client> findClientById(@ApiParam(value = "id of the client", required = true)
                                                 @NonNull @PathVariable Long id) {
        log.info("Find Client by passing ID of client, where client ID is :{} ", id);
        Optional<Client> client = (clientService.findClientById(id));
        if (client.isEmpty()) log.warn("Client with id {} is not found.", id);
        else log.debug("Client with id {} is found: {}", id, client);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Saves the client into the database",
            notes = "If provided valid client saves it",
            response = Client.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The client is successfully saved"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Client> saveClient(@Valid @RequestBody Client client, BindingResult bindingResult) throws Exception {
        log.info("Create new client by passing : {}", client);
        if (bindingResult.hasErrors()) {
            log.error("New client is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        Client clientSaved = clientService.saveClient(client);
        log.debug("New client is created: {}", clientSaved);
        return new ResponseEntity<>(clientSaved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/deactivate")
    @ApiOperation(value = "Deactivate the client by id",
            notes = "Deactivates the client if provided id exists",
            response = Client.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The client is successfully deactivated"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> deactivateClientById(@ApiParam(value = "The id of the client", required = true)
                                                     @NonNull @PathVariable Long id) {
        log.info("Deactivate Client by passing ID, where ID is:{}", id);
        Optional<Client> client = clientService.findClientById(id);
        if (client.isEmpty()) {
            log.warn("Client for deactivating with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        clientService.deactivateClient(client.get());
        log.debug("Client with id {} is deactivated: {}", id, client);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/activate")
    @ApiOperation(value = "Activates the client by id",
            notes = "Activates the client if provided id exists",
            response = Client.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The client is successfully activated"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> activateClientById(@ApiParam(value = "The id of the client", required = true)
                                                   @NonNull @PathVariable Long id) {
        log.info("Activate Client by passing ID, where ID is:{}", id);
        Optional<Client> client = clientService.findClientById(id);
        if (client.isEmpty()) {
            log.warn("Client for activating with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        clientService.activateClient(client.get());
        log.debug("Client with id {} is activated: {}", id, client);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the client by id",
            notes = "Updates the client if provided id exists",
            response = Client.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The client is successfully updated"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Client> updateClientById(@ApiParam(value = "The id of the client", required = true)
                                                 @NonNull @PathVariable Long id,
                                                 @Valid @RequestBody Client client, BindingResult bindingResult) throws Exception{
         log.info("Update existing Client with ID: {} and new body: {}", id, client);
         if (bindingResult.hasErrors() || !id.equals(client.getId())) {
             log.warn("Client for update with id {} not found", id);
             return ResponseEntity.notFound().build();
         }
         clientService.saveClient(client);
         log.debug("Client with id {} is updated: {}", id, client);
         return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

}