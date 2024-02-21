package com.clientservice.clientservice.business.repository;

import com.clientservice.clientservice.business.repository.model.ClientDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ClientRepository extends JpaRepository<ClientDAO, Long>{

    public List<ClientDAO> findByIsActiveTrue();

}
