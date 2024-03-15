package com.clientservice.clientservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;

@ApiModel(description = "Model of brand data ")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Brand {

    @ApiModelProperty(notes = "The unique id of the car brand")
    private Long id;

    @ApiModelProperty(notes = "Name of the car brand")
    @NotEmpty
    private String name;
}
