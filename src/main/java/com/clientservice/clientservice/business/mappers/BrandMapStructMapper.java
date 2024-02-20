package com.clientservice.clientservice.business.mappers;

import com.clientservice.clientservice.business.repository.model.BrandDAO;
import com.clientservice.clientservice.model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface BrandMapStructMapper {

    BrandDAO brandToBrandDAO(Brand brand);

    Brand brandDAOToBrand(BrandDAO brandDAO);
}
