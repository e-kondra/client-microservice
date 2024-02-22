package com.clientservice.clientservice.business.mappers;

import com.clientservice.clientservice.business.repository.model.CompanyDetailsDAO;
import com.clientservice.clientservice.model.CompanyDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ClientMapStructMapper.class})
public interface CompanyDetailsMapStructMapper {

    CompanyDetailsDAO companyDetailsToCompanyDetailsDAO (CompanyDetails companyDetails);

    @Mapping(target = "client", source = "client")
    CompanyDetails companyDetailsDAOToCompanyDetails(CompanyDetailsDAO companyDetailsDAO);
}
