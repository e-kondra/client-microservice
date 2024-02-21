package com.clientservice.clientservice.web.controller;

import com.clientservice.clientservice.business.service.CarService;
import com.clientservice.clientservice.model.Car;
import com.clientservice.clientservice.swagger.DescriptionVariables;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Api(tags = {DescriptionVariables.CAR})
@Log4j2
@RestController
@RequestMapping("/api/v1/car")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    @ApiOperation(value = "Finds all cars",
            notes = "Returns the entire list of cars",
            response = Car.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded", response = Car.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<List<Car>> findAllCars() {
        log.info("Retrieve list of cars");
        List<Car> carList = carService.findAllCars();
        if (carList.isEmpty()) log.warn("Car list is empty! {}", carList);
        else log.debug("Car list is found. Size: {}", carList::size);
        return ResponseEntity.ok(carList);
    }

    @GetMapping("/client/{id}")
    @ApiOperation(value = "Finds all client's cars",
            notes = "Returns the entire list of client's cars",
            response = Car.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded", response = Car.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<List<Car>> findAllCarsByClientId(@ApiParam(value = "id of the client", required = true)
                                                               @NonNull @PathVariable Long id) {
        log.info("Retrieve list of clients cars");
        List<Car> carList = carService.findAllCarsByClientId(id);
        if (carList.isEmpty()) log.warn("Clients car list is empty! {}", carList);
        else log.debug("Clients car list is found. Size: {}", carList::size);
        return ResponseEntity.ok(carList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find the car by id",
            notes = "Provide an id to search specific car in database",
            response = Car.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<Car> findCarById(@ApiParam(value = "id of the car", required = true)
                                                 @NonNull @PathVariable Long id) {
        log.info("Find car by passing ID of car, where car ID is :{} ", id);
        Optional<Car> car = (carService.findCarById(id));
        if (car.isEmpty()) log.warn("Car with id {} is not found.", id);
        else log.debug("Car with id {} is found: {}", id, car);
        return car.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Saves the car in database",
            notes = "If provided valid car, saves it",
            response = Car.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The car is successfully saved"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Car> saveCar(@Valid @RequestBody Car car, BindingResult bindingResult) throws Exception {
        log.info("Create new Car by passing : {}", car);
        if (bindingResult.hasErrors()) {
            log.error("New car is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        Car carSaved = carService.saveCar(car);
        log.debug("New car is created: {}", car);
        return new ResponseEntity<>(carSaved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the car by id",
            notes = "Updates the car if provided id exists",
            response = Car.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The car is successfully updated"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Car> updateCarById(@ApiParam(value = "id of the car", required = true)
                                                       @NonNull @PathVariable Long id,
                                                       @Valid @RequestBody Car car, BindingResult bindingResult) throws Exception {
        log.info("Update existing Car with ID: {} and new body: {}", id, car);
        if (bindingResult.hasErrors() || !id.equals(car.getId())) {
            log.warn("Car for update with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        carService.saveCar(car);
        log.debug("Car with id {} is updated: {}", id, car);
        return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes the car by id",
            notes = "Deletes the car if provided id exists",
            response = Car.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The car is successfully deleted"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEmployeeById(@ApiParam(value = "The id of the car", required = true)
                                                   @NonNull @PathVariable Long id) {
        log.info("Delete Car by passing ID, where ID is:{}", id);
        Optional<Car> car = carService.findCarById(id);
        if (!(car.isPresent())) {
            log.warn("Car for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        carService.deleteCarById(id);
        log.debug("Car with id {} is deleted: {}", id, car);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


















}
