package com.clientservice.clientservice.business.service.impl;

import com.clientservice.clientservice.business.mappers.CarMapStructMapper;
import com.clientservice.clientservice.business.repository.CarRepository;
import com.clientservice.clientservice.business.repository.ClientRepository;
import com.clientservice.clientservice.business.repository.model.CarDAO;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.business.service.CarService;
import com.clientservice.clientservice.model.Car;
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
    ClientRepository clientRepository;

    @Autowired
    CarMapStructMapper carMapper;


    @Override
    public List<Car> findAllCars() {
        List<CarDAO> carDAOList = carRepository.findAll();
        log.info("Get client list. Size is: {}", carDAOList::size);
        return carDAOList.stream().map(carMapper::carDAOToCar).collect(Collectors.toList());
    }

        public List<Car> findAllCarsByClientId(Long clientId) {
        Optional<ClientDAO> client = clientRepository.findById(clientId);
        List<CarDAO> carDAOList = carRepository.findByClientId(client.get());
        log.info("Get client list. Size is: {}", carDAOList::size);
        return carDAOList.stream().map(carMapper::carDAOToCar).collect(Collectors.toList());
    }

    @Override
    public Optional<Car> findCarById(Long id) {
        Optional<Car> carById = carRepository.findById(id)
                .flatMap(carDAO -> Optional.ofNullable(carMapper.carDAOToCar(carDAO)));
        log.info("Car with id {} is {}", id, carById);
        return carById;
    }

    @Override
    public Car saveCar(Car car) {
        CarDAO carSaved = carRepository.save(carMapper.carToCarDAO(car));
        log.info("New client saved: {}", () -> carSaved);
        return carMapper.carDAOToCar(carSaved);
    }

    @Override
    public void deleteCarById(Long id) {
        carRepository.deleteById(id);
        log.info("Car with id {} is deleted", id);
    }
}
