package com.clientservice.clientservice.web.controller;

import com.clientservice.clientservice.business.service.ClientService;
import com.clientservice.clientservice.business.service.CompanyDetailsService;
import com.clientservice.clientservice.model.Client;
import com.clientservice.clientservice.model.CompanyDetails;
import com.clientservice.clientservice.swagger.DescriptionVariables;
import com.clientservice.clientservice.swagger.HTMLResponseMessages;
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

import java.util.Optional;

@Api(tags = {DescriptionVariables.COMPANY_DETAILS})
@Log4j2
@RestController
@RequestMapping("/api/v1/details")
public class CompanyDetailsController {

    @Autowired
    CompanyDetailsService detailsService;
    @Autowired
    ClientService clientService;

    @GetMapping("/{id}")
    @ApiOperation(value = "Find the company details by id",
            notes = "Provide an id to search company details in database",
            response = CompanyDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<CompanyDetails> findCompanyDetailsById(@ApiParam(value = "id of the client", required = true)
                                                                       @NonNull @PathVariable Long id) {
        log.info("Find Company Details by passing ID of Company Details , where Company Details ID is :{} ", id);
        Optional<CompanyDetails> companyDetails = detailsService.findCompanyDetailsById(id);
        if (companyDetails.isEmpty()) log.warn("Company Details by id {} is not found.", id);
        else log.debug("Company Details with id {} is found: {}", id, companyDetails);
        return companyDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/client/{id}")
    @ApiOperation(value = "Find the company details by client id",
            notes = "Provide an client id to search associated company details in database",
            response = CompanyDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<CompanyDetails> findCompanyDetailsByClientId(@ApiParam(value = "id of the client", required = true)
                                                 @NonNull @PathVariable Long id) {
        log.info("Find CompanyDetails by passing ID of client, where client ID is :{} ", id);
        Optional<Client> client = (clientService.findClientById(id));
        if (client.isEmpty()) {
            log.warn("Client with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        else log.debug("Client with id {} is found: {}", id, client);

        Optional<CompanyDetails> companyDetails = detailsService.findCompanyDetailsByClient(client.get());

        if (companyDetails.isEmpty()) log.warn("CompanyDetails by client id {} is not found.", id);
        else log.debug("CompanyDetails with client id {} is found: {}", id, companyDetails);
        return companyDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping
    @ApiOperation(value = "Saves the companyDetails into the database",
            notes = "If provided valid companyDetails saves it",
            response = CompanyDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CompanyDetails> saveCompanyDetails(@Valid @RequestBody CompanyDetails details, BindingResult bindingResult) throws Exception {
        log.info("Create new company details by passing : {}", details);
        if (bindingResult.hasErrors()) {
            log.error("New company details is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        CompanyDetails companyDetails = detailsService.saveCompanyDetails(details);
        log.debug("New company details is created: {}", companyDetails);
        return new ResponseEntity<>(companyDetails, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the company details by id",
            notes = "Updates the company details if provided id exists",
            response = CompanyDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CompanyDetails> updateCompanyDetailsById(@ApiParam(value = "The id of the client", required = true)
                                                   @NonNull @PathVariable Long id,
                                                   @Valid @RequestBody CompanyDetails details, BindingResult bindingResult) throws Exception{
        log.info("Update existing Company Details with ID: {} and new body: {}", id, details);
        if (bindingResult.hasErrors() || !id.equals(details.getId())) {
            log.warn("Company Details for update with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        detailsService.saveCompanyDetails(details);
        log.debug("Company Details with id {} is updated: {}", id, details);
        return new ResponseEntity<>(details, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes the company details by id",
            notes = "Deletes the company details if provided id exists",
            response = CompanyDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITHOUT_DATA),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCompanyDetailsById(@ApiParam(value = "The id of the company details", required = true)
                                                   @NonNull @PathVariable Long id) {
        log.info("Delete Company Details by passing ID, where ID is:{}", id);
        Optional<CompanyDetails> details = detailsService.findCompanyDetailsById(id);
        if (details.isEmpty()) {
            log.warn("Company Details for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        detailsService.deleteCompanyDetailsById(id);
        log.debug("Company Details with id {} is deleted: {}", id, details);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
