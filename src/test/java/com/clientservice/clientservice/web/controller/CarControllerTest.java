package com.clientservice.clientservice.web.controller;

import com.clientservice.clientservice.business.service.impl.CarServiceImpl;
import com.clientservice.clientservice.model.Brand;
import com.clientservice.clientservice.model.Car;
import com.clientservice.clientservice.model.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CarController.class)
class CarControllerTest {

    public static String URL = "/api/v1/car";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BindingResult bindingResult;

    @Autowired
    private CarController controller;

    @MockBean
    private CarServiceImpl service;

    @Test
    void findAllCars() throws Exception {

        List<Car> carList = createCarList();

        when(service.findAllCars()).thenReturn(carList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value("Corolla"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number").value("ABC123"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAllCars();
    }

    @Test
    void findAllCarsByClientId() throws Exception {

        List<Car> carList = createCarList();

        when(service.findAllCarsByClientId(createClient().getId())).thenReturn(carList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/client/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value("Corolla"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number").value("ABC123"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAllCarsByClientId(anyLong());
    }

    @Test
    void findCarById() throws Exception{

        Optional<Car> optionalCar = Optional.of(createCar());

        when(service.findCarById(anyLong())).thenReturn(optionalCar);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Corolla"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("ABC123"))
                .andExpect(status().isOk());

        verify(service, times(1)).findCarById(anyLong());
    }

    @Test
    void findCarByIdInvalid() throws Exception {
        Optional<Car> carOptional = Optional.of(createCar());
        carOptional.get().setId(null);

        when(service.findCarById(null)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + null)
                        .content(asJsonString(carOptional))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).findCarById(null);
    }

    @Test
    void saveCar() throws Exception{
        Car car = createCar();
        car.setId(null);

        when(service.saveCar(car)).thenReturn(car);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(car))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveCar(car);
    }
    @Test
    void saveCarInvalid() throws Exception{
        Car car = createCar();
//        Brand brand = null;
//        car.setBrand(brand);

        when(service.saveCar(car)).thenReturn(null);
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(car))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).saveCar(car);
    }

    @Test
    void updateCarById() throws Exception {
        Car car = createCar();

        when(service.findCarById(car.getId())).thenReturn(Optional.of(car));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(car))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveCar(car);
    }

    @Test
    void UpdateCarByIdInvalid() throws Exception {
        Car car = createCar();
        //car.setId(null);

        when(service.findCarById(car.getId())).thenReturn(Optional.of(car));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/2")
                        .content(asJsonString(car))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(service, times(0)).saveCar(car);
    }

    @Test
    void deleteCarById() throws Exception{
        Optional<Car> optionalCar = Optional.of(createCar());
        when(service.findCarById(anyLong())).thenReturn(optionalCar);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(optionalCar))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteCarById(anyLong());
    }


    @Test
    void deleteCarByIdInvalidEX() throws Exception {
        when(service.findCarById(anyLong())).thenReturn(Optional.empty());
        ResultActions mvcRes = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/333"))
                .andExpect(status().isNotFound());

        verify(service, times(0)).deleteCarById(anyLong());
    }

    private List<Car> createCarList() {
        List<Car> carList = new ArrayList<>();
        carList.add(createCar());
        carList.add(createCar());
        return carList;
    }


    private Car createCar() {
        Brand brand = new Brand(1L, "Toyota");
        Client client = createClient();
        return new Car(1L,"Corolla", "ABC123", brand, client);
    }

    private Client createClient() {
        Client client_ =  new Client();
        client_.setId(1L);
        client_.setName("UAB Nova");
        client_.setAddress("Vilnius, Kauno g. 123");
        client_.setEmail("nova@gmail.com");
        client_.setIsActive(true);
        client_.setCarIds(null);
        return client_;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}