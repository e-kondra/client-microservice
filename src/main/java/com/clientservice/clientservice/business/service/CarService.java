package com.clientservice.clientservice.business.service;

import com.clientservice.clientservice.model.Car;

import java.util.List;

public interface CarService {
    List<Car> findAllCars();

    List<Car> findAllCarsByClientId(Long clientId);
}
