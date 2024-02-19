package com.clientservice.clientservice.web.controller;

import com.clientservice.clientservice.swagger.DescriptionVariables;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {DescriptionVariables.CAR})
@Log4j2
@RestController
@RequestMapping("/api/v1/car")
public class CarController {
}
