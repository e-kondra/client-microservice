package com.clientservice.clientservice.business.repository;

import com.clientservice.clientservice.business.repository.model.CarDAO;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<CarDAO, Long> {
    List<CarDAO> findByClientId(ClientDAO clientDAO);

}
