package com.clientservice.clientservice.business.service;

import com.clientservice.clientservice.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {
    List<Car> findAllCars();

    List<Car> findAllCarsByClientId(Long clientId);

    Optional<Car> findCarById(Long id);

    Car saveCar(Car car);

    void deleteCarById(Long id);
}
