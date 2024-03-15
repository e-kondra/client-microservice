package com.clientservice.clientservice.business.service.impl;

import com.clientservice.clientservice.business.mappers.BrandMapStructMapper;
import com.clientservice.clientservice.business.repository.BrandRepository;
import com.clientservice.clientservice.business.repository.model.BrandDAO;
import com.clientservice.clientservice.model.Brand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {

    @Mock
    BrandRepository brandRepository;
    @InjectMocks
    private BrandServiceImpl service;
    @Mock
    private BrandMapStructMapper mapper;

    private List<BrandDAO> brandDAOS;
    private BrandDAO brandDAO;
    private Brand brand;

    @BeforeEach
    public void init(){
        brandDAO = new BrandDAO(1l, "TOYOTA");
        brandDAOS = this.createBrandDAOS();
        brand = new Brand(1L,"TOYOTA");
    }

    private List<BrandDAO> createBrandDAOS() {
        List<BrandDAO> brandDAOList = new ArrayList<>();
        brandDAOList.add(brandDAO);
        brandDAOList.add(new BrandDAO(2L, "BMW"));
        return brandDAOList;
    }

    @Test
    void findAllBrands() {
        when(brandRepository.findAll()).thenReturn(brandDAOS);
        when(mapper.brandDAOToBrand(brandDAO)).thenReturn(brand);

        List<Brand> brandList = service.findAllBrands();
        assertEquals(2, brandList.size());
        verify(brandRepository, times(1)).findAll();
    }

    @Test
    void findAllBrandsListIsEmpty() {
        when(brandRepository.findAll()).thenReturn(Collections.emptyList());

        Assertions.assertTrue(service.findAllBrands().isEmpty());
        verify(brandRepository, times(1)).findAll();
    }

    @Test
    void findBrandById() {
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brandDAO));
        when(mapper.brandDAOToBrand(brandDAO)).thenReturn(brand);

        Optional<Brand> returnedBrand = service.findBrandById(brand.getId());
        assertEquals(brand.getId(), returnedBrand.get().getId());
        assertEquals(brand.getName(), returnedBrand.get().getName());
        verify(brandRepository, times(1)).findById(anyLong());
    }

    @Test
    void saveBrand() {
        when(brandRepository.save(brandDAO)).thenReturn(brandDAO);
        when(mapper.brandDAOToBrand(brandDAO)).thenReturn(brand);
        when(mapper.brandToBrandDAO(brand)).thenReturn(brandDAO);
        Brand savedBrand = service.saveBrand(brand);

        assertTrue(service.hasNoMatch(savedBrand));
        assertEquals(brand, savedBrand);
        verify(brandRepository, times(1)).save(brandDAO);
    }

    @Test
    void saveBrandInvalid() {
        when(brandRepository.save(brandDAO)).thenThrow(new IllegalArgumentException());
        when(mapper.brandToBrandDAO(brand)).thenReturn(brandDAO);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.saveBrand(brand));
        verify(brandRepository, times(1)).save(brandDAO);
    }
    @Test
    void SaveBrandInvalidDuplicate() {
        Brand brandForSaving = new Brand(null, "TOYOTA");
        when(brandRepository.findAll()).thenReturn(brandDAOS);
        assertThrows(HttpClientErrorException.class, () -> service.saveBrand(brandForSaving));
        verify(brandRepository, times(0)).save(brandDAO);
    }

    @Test
    void deleteBrandById() {
        service.deleteBrandById(anyLong());
        verify(brandRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteBrandByIdInvalid() {
        doThrow(new IllegalArgumentException()).when(brandRepository).deleteById(anyLong());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.deleteBrandById(anyLong()));
    }
}