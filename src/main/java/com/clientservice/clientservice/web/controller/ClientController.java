package com.clientservice.clientservice.web.controller;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.clientservice.clientservice.swagger.DescriptionVariables;

@Api(tags = {DescriptionVariables.CLIENT})
@Log4j2
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {
}
