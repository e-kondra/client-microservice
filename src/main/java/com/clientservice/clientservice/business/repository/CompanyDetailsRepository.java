package com.clientservice.clientservice.business.repository;

import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.business.repository.model.CompanyDetailsDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyDetailsRepository extends JpaRepository<CompanyDetailsDAO, Long> {

    Optional<CompanyDetailsDAO> findByClient (ClientDAO client);

}
