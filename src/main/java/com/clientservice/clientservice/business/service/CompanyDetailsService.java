package com.clientservice.clientservice.business.service;

import com.clientservice.clientservice.model.Client;
import com.clientservice.clientservice.model.CompanyDetails;

import java.util.Optional;

public interface CompanyDetailsService {

    Optional<CompanyDetails> findCompanyDetailsByClient(Client client);

    CompanyDetails saveCompanyDetails(CompanyDetails details);

    Optional<CompanyDetails> findCompanyDetailsById(Long id);

    void deleteCompanyDetailsById(Long id);
}
