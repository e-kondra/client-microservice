package com.clientservice.clientservice.business.service;

import com.clientservice.clientservice.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAllClients();

    Optional<Client> findClientById(Long id);
}
