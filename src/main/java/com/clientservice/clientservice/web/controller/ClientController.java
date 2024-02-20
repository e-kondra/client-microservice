package com.clientservice.clientservice.web.controller;

import com.clientservice.clientservice.business.service.ClientService;
import com.clientservice.clientservice.model.Client;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.clientservice.clientservice.swagger.DescriptionVariables;

import java.util.List;

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
        if (clientList.isEmpty()) {
            log.warn("Client list is empty! {}", clientList);
        }
        else {
            log.debug("Client list is found. Size: {}", clientList::size);
        }
        return ResponseEntity.ok(clientList);
    }
}
