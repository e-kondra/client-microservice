package com.clientservice.clientservice.web.controller;

import com.clientservice.clientservice.business.service.CarService;
import com.clientservice.clientservice.model.Car;
import com.clientservice.clientservice.swagger.DescriptionVariables;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        if (carList.isEmpty()) {
            log.warn("Car list is empty! {}", carList);
        } else {
            log.debug("Car list is found. Size: {}", carList::size);
        }
        return ResponseEntity.ok(carList);
    }

    @GetMapping("/client/{id}")
    @ApiOperation(value = "Finds all client's cars",
            notes = "Returns the entire list of cars",
            response = Car.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded", response = Car.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<List<Car>> findAllCarsByClientId(@ApiParam(value = "id of the client", required = true)
                                                               @NonNull @PathVariable Long clientId) {
        log.info("Retrieve list of Clients");
        List<Car> carList = carService.findAllCarsByClientId(clientId);
        if (carList.isEmpty()) {
            log.warn("Car list is empty! {}", carList);
        } else {
            log.debug("Car list is found. Size: {}", carList::size);
        }
        return ResponseEntity.ok(carList);
    }
}
