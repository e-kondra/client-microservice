package com.clientservice.clientservice.business.service.impl;

import com.clientservice.clientservice.business.mappers.ClientMapStructMapper;
import com.clientservice.clientservice.business.repository.ClientRepository;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.business.service.ClientService;
import com.clientservice.clientservice.model.Client;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
    public List<Client> findAllActiveClients() {
        List<ClientDAO> clientDAOList = clientRepository.findByIsActiveTrue();
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

    @Override
    public Client saveClient(Client client) {
        if (!hasNoMatch(client)) {
            log.error("Client conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        ClientDAO clientSaved = clientRepository.save(clientMapper.clientToClientDAO(client));
        log.info("New client saved: {}", () -> clientSaved);
        return clientMapper.clientDAOToClient(clientSaved);
    }

    @Override
    public void deactivateClient(Client client) {
        client.setIsActive(false);
        clientRepository.save(clientMapper.clientToClientDAO(client));
        log.info("Client with id {} is deactivated", client.getId());
    }

    @Override
    public void activateClient(Client client) {
        client.setIsActive(true);
        clientRepository.save(clientMapper.clientToClientDAO(client));
        log.info("Client with id {} is deactivated", client.getId());
    }

    public boolean hasNoMatch(Client client) {
        return clientRepository.findAll().stream()
                .noneMatch(clientDAO -> !clientDAO.getId().equals(client.getId()) &&
                        clientDAO.getName().equalsIgnoreCase(client.getName()));
    }
}