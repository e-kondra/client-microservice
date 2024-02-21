package com.clientservice.clientservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;


@ApiModel(description = "Model of client data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private static final String CONTAIN_LETTERS_MESSAGE = "Type must contain only letters";
    private static final String CONTAIN_EMAIL_MESSAGE = "Type must contain letters, @, point, may contain numbers";
    private static final String STRING_PATTERN = "^[a-zA-Z\\s]*$";
    private static final String EMAIL_PATTERN = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b";

    @ApiModelProperty(notes = "The unique id of the client")
    private Long id;

    @ApiModelProperty(notes = "Name of training")
    @NonNull
    @NotEmpty
    @Pattern(regexp = STRING_PATTERN, message = CONTAIN_LETTERS_MESSAGE)
    private String name;

    @ApiModelProperty(notes = "Address of client")
    @NonNull
    @NotEmpty
    private String address;

    @ApiModelProperty(notes = "Email of client")
    @Pattern(regexp = EMAIL_PATTERN, message = CONTAIN_EMAIL_MESSAGE)
    private String email;

    @ApiModelProperty(notes = "Phone number of client")
    private String phoneNumber;

    @ApiModelProperty(notes = "Indication of live client")
    @NonNull
    private Boolean isActive;

    @ApiModelProperty(notes = "client's cars")
    private List<Long> carIds;


}
