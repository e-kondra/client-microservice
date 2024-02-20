package com.clientservice.clientservice.business.mappers;

import com.clientservice.clientservice.business.repository.model.CarDAO;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {
        BrandMapStructMapper.class,
        ClientMapStructMapper.class})
public interface CarMapStructMapper {

    @Mappings({
        @Mapping(source = "client", target = "clientId"),
        @Mapping(source = "brand", target = "brandId")
    })
    CarDAO carToCarDAO(Car car);

    @Mappings({
        @Mapping(source = "clientId", target = "client"),
        @Mapping(source = "brandId", target = "brand")
    })
    Car carDAOToCar(CarDAO carDAO);

}

