package com.clientservice.clientservice.business.service;

import com.clientservice.clientservice.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> findAllBrands();

    Optional<Brand> findBrandById(Long id);

    Brand saveBrand(Brand brand);

    void deleteBrandById(Long id);
}
