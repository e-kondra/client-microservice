package com.clientservice.clientservice.web.controller;


import com.clientservice.clientservice.business.service.impl.BrandServiceImpl;
import com.clientservice.clientservice.model.Brand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BrandController.class)
class BrandControllerTest {

    public static String URL = "/api/v1/brand";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandController controller;

    @MockBean
    private BrandServiceImpl service;

    @Test
    void findAllBrands() throws Exception {

        List<Brand> brandList = createBrandList();

        when(service.findAllBrands()).thenReturn(brandList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TOYOTA"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAllBrands();
    }

    @Test
    void findBrandById() throws Exception {
        Optional<Brand> optionalBrand = Optional.of(createBrand());

        when(service.findBrandById(anyLong())).thenReturn(optionalBrand);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TOYOTA"))
                .andExpect(status().isOk());

        verify(service, times(1)).findBrandById(anyLong());
    }

    @Test
    void findBrandByIdInvalid() throws Exception {
        Optional<Brand> optionalBrand = Optional.of(createBrand());
        optionalBrand.get().setId(null);

        when(service.findBrandById(null)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + null)
                        .content(asJsonString(optionalBrand))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).findBrandById(null);
    }

    @Test
    void saveBrand()throws Exception {
        Brand brand = createBrand();
        brand.setId(null);

        when(service.saveBrand(brand)).thenReturn(brand);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(brand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveBrand(brand);
    }

    @Test
    void testSaveBrandInvalid() throws Exception {
        Brand brand = createBrand();
        brand.setName("");

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(brand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(service, times(0)).saveBrand(brand);
    }

    @Test
    void updateBrandById() throws Exception {
        Brand brand = createBrand();

        when(service.findBrandById(brand.getId())).thenReturn(Optional.of(brand));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(brand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveBrand(brand);
    }

    @Test
    void testUpdateBrandByIdInvalid() throws Exception {
        Brand brand = createBrand();
        brand.setId(null);

        when(service.findBrandById(null)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/")
                        .content(asJsonString(brand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(service, times(0)).saveBrand(brand);
    }

    @Test
    void deleteBrandById() throws Exception {
        Optional<Brand> optionalBrand = Optional.of(createBrand());
        when(service.findBrandById(anyLong())).thenReturn(optionalBrand);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(optionalBrand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteBrandById(anyLong());
    }

    @Test
    void deleteBrandByIdInvalid() throws Exception {
        Optional<Brand> optionalBrand = Optional.of(createBrand());
        optionalBrand.get().setId(null);

        when(service.findBrandById(null)).thenReturn(optionalBrand);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + null)
                        .content(asJsonString(optionalBrand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).deleteBrandById(null);
    }

    private Brand createBrand(){
        return new Brand(1l, "TOYOTA");
    }

    private List<Brand> createBrandList() {
        List<Brand> brandList = new ArrayList<>();
        brandList.add(createBrand());
        brandList.add(createBrand());
        return brandList;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}