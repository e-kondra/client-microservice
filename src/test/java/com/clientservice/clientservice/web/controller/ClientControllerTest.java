package com.clientservice.clientservice.web.controller;

import com.clientservice.clientservice.business.service.impl.ClientServiceImpl;
import com.clientservice.clientservice.model.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    public static String URL = "/api/v1/client";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BindingResult bindingResult;

    @Autowired
    private ClientController controller;

    @MockBean
    private ClientServiceImpl service;


    private List<Client> createClientList() {
        List<Client> clients = new ArrayList<>();
        clients.add(createClient());
        clients.add(createNotActiveClient());
        return clients;
    }

    private List<Client> createActiveClientList() {
        List<Client> clients = new ArrayList<>();
        clients.add(createClient());
        clients.add(createClient());
        clients.add(createClient());
        return clients;
    }

    private Client createClient() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Client");
        client.setIsActive(true);
        client.setEmail("client@gmail.com");
        client.setPhoneNumber("+370261261122");
        client.setAddress("Vilnius, Vilniaus g. 123");
        return client;
    }
    private Client createNotActiveClient() {
        Client client = new Client();
        client.setId(2L);
        client.setName("Client");
        client.setIsActive(false);
        client.setEmail("client@gmail.com");
        client.setPhoneNumber("+370261261122");
        client.setAddress("Vilnius, Vilniaus g. 123");
        return client;
    }

    @Test
    void findAllClients() throws Exception{
        List<Client> clientList = createClientList();

        when(service.findAllClients()).thenReturn(clientList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Client"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("client@gmail.com"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAllClients();
    }

    @Test
    void findAllActiveClients() throws Exception{
        List<Client> clientList = createActiveClientList();

        when(service.findAllActiveClients()).thenReturn(clientList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/active"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(status().isOk());

        verify(service, times(1)).findAllActiveClients();
    }

    @Test
    void findClientById() throws Exception{

        Optional<Client> optionalClient = Optional.of(createClient());

        when(service.findClientById(anyLong())).thenReturn(optionalClient);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Client"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("client@gmail.com"))
                .andExpect(status().isOk());

        verify(service, times(1)).findClientById(anyLong());
    }

    @Test
    void findClientByIdInvalid() throws Exception {
        Optional<Client> clientOptional = Optional.of(createClient());
        clientOptional.get().setId(null);

        when(service.findClientById(null)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + null)
                        .content(asJsonString(clientOptional))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).findClientById(null);
    }

    @Test
    void saveClient() throws Exception{
        Client client = createClient();
        client.setId(null);

        when(service.saveClient(client)).thenReturn(client);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveClient(client);
    }

    @Test
    void saveClientWithInvalidBindingResult() throws Exception{
        Client client = createClient();
        client.setId(null);
        client.setName("");

        when(service.saveClient(client)).thenReturn(client);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(0)).saveClient(client);
    }

    @Test
    void deactivateClientById() throws Exception{
        Client clientNotActive = createNotActiveClient();

        when(service.findClientById(clientNotActive.getId())).thenReturn(Optional.of(clientNotActive));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/2/deactivate")
                        .content(asJsonString(clientNotActive))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deactivateClient(clientNotActive);
    }

    @Test
    void deactivateClientByIdWithInvalidClient() throws Exception{
        Client clientNotActive = createNotActiveClient();

        when(service.findClientById(clientNotActive.getId())).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/2/deactivate")
                        .content(asJsonString(clientNotActive))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).deactivateClient(clientNotActive);

    }

    @Test
    void activateClientById()throws Exception{
        Client client = createClient();

        when(service.findClientById(client.getId())).thenReturn(Optional.of(client));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1/activate")
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).activateClient(client);
    }

    @Test
    void activateClientByIdWithInvalidClient()throws Exception{
        Client client = createClient();

        when(service.findClientById(client.getId())).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1/activate")
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).activateClient(client);
    }

    @Test
    void updateClientById() throws Exception{
        Client client = createClient();
        client.setIsActive(false);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Client"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(false))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveClient(client);
    }

    @Test
    void updateClientByIdWithInvalidBindingResult() throws Exception{
        Client client = createClient();
        client.setName("");

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).saveClient(client);
    }

    @Test
    void updateClientByIdWithInvalidId() throws Exception{
        Client client = createClient();
        client.setId(555L);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).saveClient(client);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}