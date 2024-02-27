package com.clientservice.clientservice.web.controller;

import com.clientservice.clientservice.business.service.impl.BrandServiceImpl;
import com.clientservice.clientservice.model.Brand;
import com.clientservice.clientservice.swagger.DescriptionVariables;
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

import java.util.List;
import java.util.Optional;

@Api(tags = {DescriptionVariables.BRAND})
@Log4j2
@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {

    @Autowired
    private BrandServiceImpl brandService;

    @GetMapping
    @ApiOperation(value = "Finds all brands",
            notes = "Returns the entire list of brands",
            response = Brand.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded", response = Brand.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<List<Brand>> findAllBrands() {
        log.info("Retrieve list of Brands");
        List<Brand> brandList = brandService.findAllBrands();
        if (brandList.isEmpty()) log.warn("Brand list is empty! {}", brandList);
        else log.debug("Brand list is found. Size: {}", brandList::size);
        log.debug("Brand list is found. Size: {}", brandList::size);
        return ResponseEntity.ok(brandList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find the brand by id",
            notes = "Provide an id to search specific brand in database",
            response = Brand.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<Brand> findBrandById(@ApiParam(value = "id of the brand", required = true)
                                                     @NonNull @PathVariable Long id) {
        log.info("Find Brand by passing ID of brand, where brand ID is :{} ", id);
        Optional<Brand> brand = (brandService.findBrandById(id));
        if (brand.isEmpty()) log.warn("Brand with id {} is not found.", id);
        else log.debug("Brand with id {} is found: {}", id, brand);
        return brand.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Saves the brand in database",
            notes = "If provided valid brand, saves it",
            response = Brand.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The brand is successfully saved"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Brand> saveBrand(@Valid @RequestBody Brand brand, BindingResult bindingResult) throws Exception {
        log.info("Create new Brand by passing : {}", brand);
        if (bindingResult.hasErrors()) {
            log.error("New Brand is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        Brand brandSaved = brandService.saveBrand(brand);
        log.debug("New brand is created: {}", brand);
        return new ResponseEntity<>(brandSaved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the brand by id",
            notes = "Updates the brand if provided id exists",
            response = Brand.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The brand is successfully updated"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Brand> updateBrandById(@ApiParam(value = "id of the brand", required = true)
                                                    @NonNull @PathVariable Long id,
                                                    @Valid @RequestBody Brand brand, BindingResult bindingResult) throws Exception {
        log.info("Update existing Brand with ID: {} and new body: {}", id, brand);
        if (bindingResult.hasErrors() || !id.equals(brand.getId())) {
            log.warn("Brand for update with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        brandService.saveBrand(brand);
        log.debug("Brand with id {} is updated: {}", id, brand);
        return new ResponseEntity<>(brand, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes the brand by id",
            notes = "Deletes the brand if provided id exists",
            response = Brand.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The brand is successfully deleted"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBrandById(@ApiParam(value = "The id of the brand", required = true)
                                                   @NonNull @PathVariable Long id) {
        log.info("Delete Brand by passing ID, where ID is:{}", id);
        Optional<Brand> brand = brandService.findBrandById(id);
        if (brand.isEmpty()) {
            log.warn("Brand for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        brandService.deleteBrandById(id);
        log.debug("Brand with id {} is deleted: {}", id, brand);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
