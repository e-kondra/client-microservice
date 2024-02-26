package com.clientservice.clientservice.business.service.impl;

import com.clientservice.clientservice.business.mappers.ClientMapStructMapper;
import com.clientservice.clientservice.business.repository.ClientRepository;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    ClientRepository clientRepository;
    @InjectMocks
    ClientServiceImpl clientService;
    @Mock
    ClientMapStructMapper clientMapper;

    ClientDAO clientDAO;
    ClientDAO clientDAO2;
    List<ClientDAO> clientDAOS;
    Client client;

    @BeforeEach
    void setUp() {
        clientDAO = createClientDAO();
        clientDAO2 = createClientDAO2();
        clientDAOS = createClientDAOSList();
        client = createClient();
    }


    private Client createClient() {
        Client client_ =  new Client();
        client_.setId(1L);
        client_.setName("UAB Nova");
        client_.setAddress("Vilnius, Kauno g. 123");
        client_.setEmail("nova@gmail.com");
        client_.setIsActive(true);
        client_.setCarIds(null);
        return client_;
    }

    private List<ClientDAO> createClientDAOSList() {
        List<ClientDAO> listClientDAO = new ArrayList<>();
        listClientDAO.add(clientDAO);
        listClientDAO.add(clientDAO2);
        return listClientDAO;
    }

    private ClientDAO createClientDAO() {
        ClientDAO clientDAO_ =  new ClientDAO();
        clientDAO_.setId(1L);
        clientDAO_.setName("UAB Nova");
        clientDAO_.setAddress("Vilnius, Kauno g. 123");
        clientDAO_.setEmail("nova@gmail.com");
        clientDAO_.setIsActive(1);
        clientDAO_.setCompanyDetailsId(null);
        clientDAO_.setCarIds(null);
        return clientDAO_;
    }
    private ClientDAO createClientDAO2() {
        ClientDAO clientDAO_ =  new ClientDAO();
        clientDAO_.setId(1L);
        clientDAO_.setName("UAB Nova");
        clientDAO_.setAddress("Vilnius, Kauno g. 123");
        clientDAO_.setEmail("nova@gmail.com");
        clientDAO_.setIsActive(0);
        clientDAO_.setCompanyDetailsId(null);
        clientDAO_.setCarIds(null);
        return clientDAO_;
    }


    @Test
    void findAllClients() {
        when(clientRepository.findAll()).thenReturn(clientDAOS);
        when(clientMapper.clientDAOToClient(clientDAO)).thenReturn(client);

        List<Client> clientList = clientService.findAllClients();
        assertEquals(2, clientList.size());
        assertEquals(1L, clientList.get(0).getId());
        assertEquals("nova@gmail.com", clientList.get(0).getEmail());

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void findAllActiveClients() {
        List<ClientDAO> clientDAOS2 = clientDAOS.stream()
                .filter(e -> e.getIsActive() == 1).collect(Collectors.toList());
        when(clientRepository.findByIsActiveTrue()).thenReturn(clientDAOS2);
        when(clientMapper.clientDAOToClient(clientDAO)).thenReturn(client);

        List<Client> clientList = clientService.findAllActiveClients();
        assertEquals(1, clientList.size());
        assertEquals(1L, clientList.get(0).getId());
        assertEquals("nova@gmail.com", clientList.get(0).getEmail());

        verify(clientRepository, times(1)).findByIsActiveTrue();
    }


    @Test
    void findClientById() {

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(clientDAO));
        when(clientMapper.clientDAOToClient(clientDAO)).thenReturn(client);

        Optional<Client> returnedClient= clientService.findClientById(client.getId());
        assertEquals(client.getId(), returnedClient.get().getId());
        assertEquals(client.getName(), returnedClient.get().getName());
        assertEquals(client.getEmail(), returnedClient.get().getEmail());
        verify(clientRepository, times(1)).findById(anyLong());
    }

    @Test
    void saveClient() {
        when(clientRepository.save(clientDAO)).thenReturn(clientDAO);
        when(clientMapper.clientToClientDAO(client)).thenReturn(clientDAO);
        when(clientMapper.clientDAOToClient(clientDAO)).thenReturn(client);

        Client clientSaved = clientService.saveClient(client);
        assertEquals(client, clientSaved);
        verify(clientRepository, times(1)).save(clientDAO);
    }

    @Test
    void saveClientInvalid() {
        when(clientRepository.save(clientDAO)).thenThrow(new IllegalArgumentException());
        when(clientMapper.clientToClientDAO(client)).thenReturn(clientDAO);
        Assertions.assertThrows(IllegalArgumentException.class, () -> clientService.saveClient(client));
        verify(clientRepository, times(1)).save(clientDAO);
    }
    @Test
    void SaveClientInvalidDuplicate() {
        Client clientForSaving = createClient();
        clientForSaving.setId(2L);
        when(clientRepository.findAll()).thenReturn(clientDAOS);
        assertThrows(HttpClientErrorException.class, () -> clientService.saveClient(clientForSaving));
        verify(clientRepository, times(0)).save(clientDAO);
    }

    @Test
    void deactivateClient() {
        when(clientRepository.save(clientDAO)).thenReturn(clientDAO);
        when(clientMapper.clientToClientDAO(client)).thenReturn(clientDAO);

        clientService.deactivateClient(client);
        assertEquals(client.getIsActive(), false);
        verify(clientRepository, times(1)).save(clientDAO);
    }

    @Test
    void activateClient() {
        client.setIsActive(false);
        when(clientRepository.save(clientDAO)).thenReturn(clientDAO);
        when(clientMapper.clientToClientDAO(client)).thenReturn(clientDAO);

        clientService.activateClient(client);
        assertEquals(client.getIsActive(), true);
        verify(clientRepository, times(1)).save(clientDAO);
    }

    @Test
    void hasNoMatch() {
    }
}