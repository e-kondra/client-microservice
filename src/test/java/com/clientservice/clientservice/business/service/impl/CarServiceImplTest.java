package com.clientservice.clientservice.business.service.impl;

import com.clientservice.clientservice.business.mappers.CarMapStructMapper;
import com.clientservice.clientservice.business.repository.CarRepository;
import com.clientservice.clientservice.business.repository.ClientRepository;
import com.clientservice.clientservice.business.repository.model.BrandDAO;
import com.clientservice.clientservice.business.repository.model.CarDAO;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.model.Brand;
import com.clientservice.clientservice.model.Car;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @InjectMocks
    CarServiceImpl carService;
    @Mock
    CarRepository carRepository;
    @Mock
    CarMapStructMapper carMapper;
    @Mock
    ClientRepository clientRepository;

    CarDAO carDAO;
    List<CarDAO> carDAOS;
    Car car;
    ClientDAO clientDAO;

    @BeforeEach
    void setUp() {
        clientDAO = createClientDAO();
        carDAO = createCarDAO();
        car = createCar();
        carDAOS = createCarDAOList();
    }

    private CarDAO createCarDAO() {
        CarDAO car = new CarDAO();
        car.setId(1L);
        car.setNumber("ABC123");
        car.setModel("Prius");
        car.setBrandId(new BrandDAO(1l, "TOYOTA"));
        car.setClientId(clientDAO);
        return car;
    }

    private Car createCar() {
        Car car = new Car();
        car.setId(1L);
        car.setNumber("ABC123");
        car.setModel("Prius");
        car.setBrand(new Brand(1l, "TOYOTA"));
        return car;
    }

    private ClientDAO createClientDAO() {
        ClientDAO clientDAO_ =  new ClientDAO();
        clientDAO_.setId(1L);
        clientDAO_.setName("UAB Nova");
        clientDAO_.setAddress("Vilnius, Kauno g. 123");
        clientDAO_.setEmail("nova@gmail.com");
        clientDAO_.setIsActive(1);
        clientDAO_.setCompanyDetailsId(null);
        clientDAO_.setCarIds(null);
        return clientDAO_;
    }

    private List<CarDAO> createCarDAOList() {
        List<CarDAO> carDAOList = new ArrayList<>();
        carDAOList.add(carDAO);
        carDAOList.add(carDAO);
        return carDAOList;
    }

    @Test
    void findAllCars() {
        when(carRepository.findAll()).thenReturn(carDAOS);
        when(carMapper.carDAOToCar(carDAO)).thenReturn(car);
        List<Car> carList = carService.findAllCars();
        assertEquals(2, carList.size());
        assertEquals(1L, carList.get(0).getBrand().getId());
        assertEquals("Prius", carList.get(1).getModel());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void findAllCarsByClientId() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(clientDAO));
        when(carRepository.findByClientId(clientDAO)).thenReturn(carDAOS);
        when(carMapper.carDAOToCar(carDAO)).thenReturn(car);

        List<Car> carList = carService.findAllCarsByClientId(clientDAO.getId());
        assertEquals(2, carList.size());
        assertEquals(1L, carList.get(0).getBrand().getId());
        assertEquals("Prius", carList.get(1).getModel());
        verify(carRepository, times(1)).findByClientId(clientDAO);
    }

    @Test
    void findCarById() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(carDAO));
        when(carMapper.carDAOToCar(carDAO)).thenReturn(car);

        Optional<Car> returnedCar= carService.findCarById(car.getId());
        assertEquals(car.getId(), returnedCar.get().getId());
        assertEquals(car.getModel(), returnedCar.get().getModel());
        assertEquals(car.getBrand().getName(), returnedCar.get().getBrand().getName());
        verify(carRepository, times(1)).findById(anyLong());
    }

    @Test
    void saveCar() {
        when(carRepository.save(carDAO)).thenReturn(carDAO);
        when(carMapper.carToCarDAO(car)).thenReturn(carDAO);
        when(carMapper.carDAOToCar(carDAO)).thenReturn(car);

        Car carSaved = carService.saveCar(car);
        assertEquals(car, carSaved);
        verify(carRepository, times(1)).save(carDAO);
    }

    @Test
    void deleteCarById() {
        carService.deleteCarById(anyLong());
        verify(carRepository,times(1)).deleteById(anyLong());
    }

    @Test
    void deleteCarByIdInvalid() {
        doThrow(new IllegalArgumentException()).when(carRepository).deleteById(anyLong());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> carService.deleteCarById(anyLong()));
    }
}