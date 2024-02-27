package com.clientservice.clientservice.web.controller;


import com.clientservice.clientservice.business.service.impl.ClientServiceImpl;
import com.clientservice.clientservice.business.service.impl.CompanyDetailsServiceImpl;
import com.clientservice.clientservice.model.Client;
import com.clientservice.clientservice.model.CompanyDetails;
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

import java.util.Optional;

import static com.clientservice.clientservice.web.controller.ClientControllerTest.asJsonString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyDetailsController.class)
class CompanyDetailsControllerTest {

    public static String URL = "/api/v1/details";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BindingResult bindingResult;

    @Autowired
    private CompanyDetailsController controller;

    @MockBean
    private ClientServiceImpl clientService;

    @MockBean
    private CompanyDetailsServiceImpl service;

    @Test
    void findCompanyDetailsById() throws Exception {
        Optional<CompanyDetails> optionalDetails = Optional.of(createDetail());

        when(service.findCompanyDetailsById(anyLong())).thenReturn(optionalDetails);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("12345"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pvm").value("LT1234567"))
                .andExpect(status().isOk());

        verify(service, times(1)).findCompanyDetailsById(anyLong());
    }

    @Test
    void findCompanyDetailsByIdWithInvalidId() throws Exception {
        Optional<CompanyDetails> optionalDetails = Optional.of(createDetail());

        when(service.findCompanyDetailsById(anyLong())).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + null))
                .andExpect(status().isNotFound());

        verify(service, times(0)).findCompanyDetailsById(anyLong());
    }

    @Test
    void findCompanyDetailsByClientId() throws Exception{
        Client client = createClient();
        when(clientService.findClientById(anyLong())).thenReturn(Optional.of(client));

        when(service.findCompanyDetailsByClient(client)).thenReturn(Optional.of(createDetail()));
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/client/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("12345"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pvm").value("LT1234567"))
                .andExpect(status().isOk());

        verify(service, times(1)).findCompanyDetailsByClient(client);
    }

    @Test
    void findCompanyDetailsByClientIdWithInvalidClientId() throws Exception{
        Client client = createClient();
        when(clientService.findClientById(anyLong())).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/client/1"))
                .andExpect(status().isNotFound());

        verify(service, times(0)).findCompanyDetailsByClient(client);
    }

    @Test
    void saveCompanyDetails() throws Exception{
        CompanyDetails details = createDetail();
        details.setId(null);

        when(service.saveCompanyDetails(details)).thenReturn(details);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(details))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveCompanyDetails(details);
    }

    @Test
    void saveCompanyDetailsWithInvalidBindingResult() throws Exception{
        CompanyDetails details = createDetail();
        details.setId(null);
        details.setCode("");

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(details))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(0)).saveCompanyDetails(details);
    }

    @Test
    void updateCompanyDetailsById() throws Exception {
        CompanyDetails details = createDetail();
        details.setPvm("LT222222222");

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(details))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.representativePerson").value("Freddie Mercury"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pvm").value("LT222222222"))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveCompanyDetails(details);
    }

    @Test
    void updateCompanyDetailsByIdWithInvalidBindingResult() throws Exception {
        CompanyDetails details = createDetail();
        details.setId(555L);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(details))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).saveCompanyDetails(details);
    }

    @Test
    void updateCompanyDetailsByIdWithInvalidId() throws Exception {
        CompanyDetails details = createDetail();
        details.setCode("");

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(details))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).saveCompanyDetails(details);
    }

    @Test
    void deleteCompanyDetailsById() throws Exception{
        Optional<CompanyDetails> optionalDetails = Optional.of(createDetail());
        when(service.findCompanyDetailsById(anyLong())).thenReturn(optionalDetails);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(optionalDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteCompanyDetailsById(anyLong());
    }

    @Test
    void deleteCompanyDetailsByIdWithInvalidId() throws Exception{

        when(service.findCompanyDetailsById(anyLong())).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).deleteCompanyDetailsById(null);
    }


    private CompanyDetails createDetail() {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setId(1L);
        companyDetails.setCode("12345");
        companyDetails.setPvm("LT1234567");
        companyDetails.setRepresentativePerson("Freddie Mercury");
        companyDetails.setClient(createClient());
        return companyDetails;
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
}