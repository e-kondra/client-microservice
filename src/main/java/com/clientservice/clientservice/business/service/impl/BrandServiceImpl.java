package com.clientservice.clientservice.business.service.impl;

import com.clientservice.clientservice.business.mappers.BrandMapStructMapper;
import com.clientservice.clientservice.business.repository.BrandRepository;
import com.clientservice.clientservice.business.repository.model.BrandDAO;
import com.clientservice.clientservice.business.service.BrandService;
import com.clientservice.clientservice.model.Brand;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandRepository brandRepository;
    @Autowired
    BrandMapStructMapper brandMapper;


    @Override
    public List<Brand> findAllBrands() {
        List<BrandDAO> brandDAOList = brandRepository.findAll();
        log.info("Get brand list. Size is: {}", brandDAOList::size);
        return brandDAOList.stream().map(brandMapper::brandDAOToBrand).collect(Collectors.toList());
    }

    @Override
    public Optional<Brand> findBrandById(Long id) {
        Optional<Brand> brandById = brandRepository.findById(id)
                .flatMap(brandDAO -> Optional.ofNullable(brandMapper.brandDAOToBrand(brandDAO)));
        log.info("Brand with id {} is {}", id, brandById);
        return brandById;
    }

    @Override
    public Brand saveBrand(Brand brand) {
        if (!hasNoMatch(brand)) {
            log.error("Brand conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        BrandDAO brandSaved = brandRepository.save(brandMapper.brandToBrandDAO(brand));
        log.info("New brand saved: {}", () -> brandSaved);
        return brandMapper.brandDAOToBrand(brandSaved);
    }

    @Override
    public void deleteBrandById(Long id) {
        brandRepository.deleteById(id);
        log.info("Brand with id {} is deleted", id);
    }

    public boolean hasNoMatch(Brand brand) {
        return brandRepository.findAll().stream()
                .noneMatch(br -> !br.getId().equals(brand.getId()) &&
                        br.getName().equalsIgnoreCase(brand.getName()));
    }

}
