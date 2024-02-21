package com.clientservice.clientservice.business.service;

import com.clientservice.clientservice.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAllClients();

    List<Client> findAllActiveClients();

    Optional<Client> findClientById(Long id);

    Client saveClient(Client client);

    void deactivateClient(Client client);

    void activateClient(Client client);
}
