package com.clientservice.clientservice.business.service.impl;

import com.clientservice.clientservice.business.mappers.ClientMapStructMapper;
import com.clientservice.clientservice.business.mappers.CompanyDetailsMapStructMapper;
import com.clientservice.clientservice.business.repository.CompanyDetailsRepository;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.business.repository.model.CompanyDetailsDAO;
import com.clientservice.clientservice.business.service.CompanyDetailsService;
import com.clientservice.clientservice.model.Client;
import com.clientservice.clientservice.model.CompanyDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
@Log4j2
public class CompanyDetailsServiceImpl implements CompanyDetailsService {

    @Autowired
    CompanyDetailsRepository detailsRepository;

    @Autowired
    CompanyDetailsMapStructMapper detailsMapper;

    @Autowired
    ClientMapStructMapper clientMapper;

    @Override
    public Optional<CompanyDetails> findCompanyDetailsByClient(Client client) {
        ClientDAO clientDAO = clientMapper.clientToClientDAO(client);
        Optional<CompanyDetails> detailsByClient  = detailsRepository.findByClient(clientDAO).flatMap(details ->
                Optional.ofNullable(detailsMapper.companyDetailsDAOToCompanyDetails(details)));
        log.info("CompanyDetails CV with clients id {} is {}", client.getId(), detailsByClient);
        return detailsByClient;
    }

    @Override
    public CompanyDetails saveCompanyDetails(CompanyDetails details) {
        if (!hasNoMatch(details)) {
            log.error("CompanyDetails conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        CompanyDetailsDAO detailsDAO = detailsRepository.save(detailsMapper.companyDetailsToCompanyDetailsDAO(details));
        log.info("New client saved: {}", () -> detailsDAO);
        return detailsMapper.companyDetailsDAOToCompanyDetails(detailsDAO);
    }

    @Override
    public Optional<CompanyDetails> findCompanyDetailsById(Long id) {
        Optional<CompanyDetails> detailsById  = detailsRepository.findById(id).flatMap(details ->
                Optional.ofNullable(detailsMapper.companyDetailsDAOToCompanyDetails(details)));
        log.info("Company Details with id {} is {}", id, detailsById);
        return detailsById;
    }

    @Override
    public void deleteCompanyDetailsById(Long id) {
        detailsRepository.deleteById(id);
        log.info("Company Details with id {} is deleted", id);
    }


    public boolean hasNoMatch(CompanyDetails details) {
        return detailsRepository.findAll().stream()
                .noneMatch(detailsDAO -> !detailsDAO.getId().equals(details.getId()) &&
                        detailsDAO.getCode().equalsIgnoreCase(details.getCode()) &&
                        detailsDAO.getPvm().equalsIgnoreCase(details.getPvm()));
    }
}
