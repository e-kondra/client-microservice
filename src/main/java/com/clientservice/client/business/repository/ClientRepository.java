package com.clientservice.client.business.repository;

import com.clientservice.client.business.repository.model.ClientDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientDAO, Long> {
}
