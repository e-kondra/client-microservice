package com.clientservice.client.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@ApiModel(description = "Model of brand data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    @ApiModelProperty(notes = "The unique id of the car brand")
    private Long id;

    @ApiModelProperty(notes = "Name of the car brand")
    @NonNull
    @NotEmpty
    private String name;
}
