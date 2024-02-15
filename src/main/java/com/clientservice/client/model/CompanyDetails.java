package com.clientservice.client.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@ApiModel(description = "Model of company (entity) details data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetails {

    @ApiModelProperty(notes = "The unique id of the company details")
    private Long id;

    @ApiModelProperty(notes = "Entity code of company")
    @NonNull
    @NotEmpty
    private String code;

    @ApiModelProperty(notes = "PVM of company")
    private String pvm;

    @ApiModelProperty(notes = "Representative person of company")
    private String representativePerson;

    @ApiModelProperty(notes = "Client model of the company", required = true)
    @NotNull
    private Client client;
}
