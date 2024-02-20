package com.clientservice.clientservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@ApiModel(description = "Model of car data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @ApiModelProperty(notes = "The unique id of the car")
    private Long id;

    @ApiModelProperty(notes = "Model of the car")
    private String model;

    @ApiModelProperty(notes = "Number of the car")
    private String number;

    @ApiModelProperty(notes = "Brand of the car")
    @NonNull
    private Brand brand;

    @ApiModelProperty(notes = "Client of the car")
    @NonNull
    private Client client;

    public Car(Long carDAO) {
        this.id = carDAO;
    }
}
