package com.clientservice.clientservice.business.service.impl;

import com.clientservice.clientservice.business.mappers.ClientMapStructMapper;
import com.clientservice.clientservice.business.repository.ClientRepository;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.business.service.ClientService;
import com.clientservice.clientservice.model.Client;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientMapStructMapper clientMapper;

    @Override
    public List<Client> findAllClients() {
        List<ClientDAO> clientDAOList = clientRepository.findAll();
        log.info("Get client list. Size is: {}", clientDAOList::size);
        return clientDAOList.stream().map(clientMapper::clientDAOToClient).collect(Collectors.toList());
    }

    @Override
    public Optional<Client> findClientById(Long id) {
        Optional<Client> clientById = clientRepository.findById(id)
                .flatMap(clientDAO -> Optional.ofNullable(clientMapper.clientDAOToClient(clientDAO)));
        log.info("Client with id {} is {}", id, clientById);
        return clientById;
    }
}