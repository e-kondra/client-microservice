package com.clientservice.clientservice.business.mappers;

import com.clientservice.clientservice.business.repository.model.CarDAO;
import com.clientservice.clientservice.business.repository.model.ClientDAO;
import com.clientservice.clientservice.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Mapper(componentModel = "spring", uses = {
        CarMapStructMapper.class})
public interface ClientMapStructMapper {

    @Mappings({
        @Mapping(target = "isActive", source = "isActive", qualifiedByName="isActive"),
        @Mapping(target = "carIds", source = "carIds", qualifiedByName="carIds")
    })
    Client clientDAOToClient(ClientDAO clientDAO);

    @Mappings({
        @Mapping(target = "isActive", source = "isActive", qualifiedByName="isActive"),
        @Mapping(target = "carIds", source = "carIds", qualifiedByName="carIds")
    })
    ClientDAO clientToClientDAO(Client client);

    @Named("isActive")
    default boolean intIsActiveToBooleanIsActive(int intIsActive){
        return intIsActive == 1;
    }

    @Named("isActive")
    default int booleanIsActiveToIntIsActive(boolean boolIsActive){
        return boolIsActive ? 1 : 0;
    }

    @Named("carIds")
    default List<CarDAO> carIdsToCarDAOS(List<Long> carIds) {
        List<CarDAO> carDAOS = new ArrayList<>();
        if (isNotEmpty(carIds))
            carIds.forEach( carId -> carDAOS.add(new CarDAO(carId)));
        return carDAOS;
    }

    @Named("carIds")
    default List<Long>  carDAOSToCarIds(List<CarDAO> carDAOS) {
        List<Long> carIds = new ArrayList<>();
        if (isNotEmpty(carDAOS))
            carDAOS.forEach( carDAO -> carIds.add(carDAO.getId()));
        return carIds;
    }

}
