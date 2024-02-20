package com.clientservice.clientservice.business.service.impl;

import com.clientservice.clientservice.business.mappers.CarMapStructMapper;
import com.clientservice.clientservice.business.mappers.ClientMapStructMapper;
import com.clientservice.clientservice.business.repository.CarRepository;
import com.clientservice.clientservice.business.repository.ClientRepository;
import com.clientservice.clientservice.business.repository.model.CarDAO;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.business.service.CarService;
import com.clientservice.clientservice.business.service.ClientService;
import com.clientservice.clientservice.model.Car;
import com.clientservice.clientservice.model.Client;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    CarRepository carRepository;

    @Autowired
    CarMapStructMapper carMapper;

    @Autowired
    ClientMapStructMapper clientMapper;

    @Autowired
    ClientRepository clientRepository;


    @Override
    public List<Car> findAllCars() {
        List<CarDAO> carDAOList = carRepository.findAll();
        log.info("Get client list. Size is: {}", carDAOList::size);
        return carDAOList.stream().map(carMapper::carDAOToCar).collect(Collectors.toList());
    }

    @Override
    public List<Car> findAllCarsByClientId(Long clientId) {
        Optional<ClientDAO> clientDAO = clientRepository.findById(clientId);
        List<CarDAO> carDAOList = carRepository.findAllByClientId(clientDAO.get());
        log.info("Get client list. Size is: {}", carDAOList::size);
        return carDAOList.stream().map(carMapper::carDAOToCar).collect(Collectors.toList());
    }
}