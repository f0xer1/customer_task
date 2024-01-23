package com.example.customerapi.web.mapper;

import com.example.customerapi.model.Customer;
import com.example.customerapi.web.dto.CustomerCreationDto;
import com.example.customerapi.web.dto.CustomerDto;
import com.example.customerapi.web.dto.CustomerUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer toEntity(CustomerCreationDto creationDto);

    CustomerDto toPayload(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer update(CustomerUpdateDto updateDto, @MappingTarget Customer customer);
}
