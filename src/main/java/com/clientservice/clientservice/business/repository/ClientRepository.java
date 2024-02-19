package com.clientservice.clientservice.business.repository;

import com.clientservice.clientservice.business.repository.model.ClientDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientDAO, Long> {
}
