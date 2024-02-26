package com.clientservice.clientservice.business.service.impl;

import com.clientservice.clientservice.business.mappers.ClientMapStructMapper;
import com.clientservice.clientservice.business.mappers.CompanyDetailsMapStructMapper;
import com.clientservice.clientservice.business.repository.ClientRepository;
import com.clientservice.clientservice.business.repository.CompanyDetailsRepository;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.business.repository.model.CompanyDetailsDAO;
import com.clientservice.clientservice.model.Client;
import com.clientservice.clientservice.model.CompanyDetails;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyDetailsServiceImplTest {

    @Mock
    CompanyDetailsRepository repository;
    @InjectMocks
    private CompanyDetailsServiceImpl service;
    @InjectMocks
    private ClientServiceImpl clientService;
    @Mock
    private CompanyDetailsMapStructMapper mapper;
    @Mock
    ClientRepository clientRepository;
    @Mock
    ClientMapStructMapper clientMapper;

    CompanyDetailsDAO detailsDAO;
    CompanyDetails details;
    ClientDAO clientDAO;
    Client clientTest;
    List<CompanyDetailsDAO> detailsDAOS;

    @BeforeEach
    void init(){
        clientDAO = createClientDAO();
        detailsDAO = new CompanyDetailsDAO(1l, "12345","LT1234567","Freddie Mercury", clientDAO);
        clientDAO.setCompanyDetailsId(detailsDAO);
        clientTest = createClient();
        details = new CompanyDetails(1l, "12345","LT1234567","Freddie Mercury",clientTest);
        detailsDAOS = createDetailsDAOS();
    }

    private List<CompanyDetailsDAO> createDetailsDAOS() {
        List<CompanyDetailsDAO> detailsDAOArrayList = new ArrayList<>();
        detailsDAOArrayList.add(detailsDAO);
        detailsDAOArrayList.add(detailsDAO);
        return detailsDAOArrayList;
    }

    ClientDAO createClientDAO(){
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
    
    Client createClient(){
        Client client_ =  new Client();
        client_.setId(1L);
        client_.setName("UAB Nova");
        client_.setAddress("Vilnius, Kauno g. 123");
        client_.setEmail("nova@gmail.com");
        client_.setIsActive(true);
        client_.setCarIds(null);
        return client_;
    }

    @Test
    void findCompanyDetailsByClient() {
        when(clientMapper.clientToClientDAO(clientTest)).thenReturn(clientDAO);
        when(repository.findByClient(clientDAO)).thenReturn(Optional.of(detailsDAO));
        when(mapper.companyDetailsDAOToCompanyDetails(detailsDAO)).thenReturn(details);

        Optional<CompanyDetails> returnedDetails = service.findCompanyDetailsByClient(clientTest);
        assertEquals(details.getId(), returnedDetails.get().getId());
        assertEquals(details.getClient().getName(), returnedDetails.get().getClient().getName());
        assertEquals(details.getCode(), returnedDetails.get().getCode());
        verify(repository, times(1)).findByClient(clientDAO);
    }


    @Test
    void saveCompanyDetails() {
        when(repository.save(detailsDAO)).thenReturn(detailsDAO);
        when(mapper.companyDetailsDAOToCompanyDetails(detailsDAO)).thenReturn(details);
        when(mapper.companyDetailsToCompanyDetailsDAO(details)).thenReturn(detailsDAO);
        CompanyDetails savedCompanyDetails = service.saveCompanyDetails(details);

        assertTrue(service.hasNoMatch(savedCompanyDetails));
        assertEquals(details, savedCompanyDetails);
        verify(repository, times(1)).save(detailsDAO);
    }

    @Test
    void saveCompanyDetailsInvalidDuplicate() {
        CompanyDetails detailsForSaving = new CompanyDetails(null,"12345","LT1234567","Freddie Mercury",null);
        when(repository.findAll()).thenReturn(detailsDAOS);
        assertThrows(HttpClientErrorException.class, () -> service.saveCompanyDetails(detailsForSaving));
        verify(repository, times(0)).save(detailsDAO);
    }

    @Test
    void findCompanyDetailsById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(detailsDAO));
        when(mapper.companyDetailsDAOToCompanyDetails(detailsDAO)).thenReturn(details);

        Optional<CompanyDetails> returnedDetails = service.findCompanyDetailsById(details.getId());
        assertEquals(details.getId(), returnedDetails.get().getId());
        assertEquals(details.getPvm(), returnedDetails.get().getPvm());
        assertEquals(details.getCode(), returnedDetails.get().getCode());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void deleteCompanyDetailsById() {
        service.deleteCompanyDetailsById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteCompanyDetailsByIdInvalid() {
        doThrow(new IllegalArgumentException()).when(repository).deleteById(anyLong());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.deleteCompanyDetailsById(anyLong()));
    }

}