package com.loans.mapper;

import com.loans.api.api.model.CustomerDTO;
import com.loans.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toEntity(CustomerDTO dto);

    CustomerDTO toDto(Customer entity);

    List<CustomerDTO> toDto(List<Customer> customers);

}
