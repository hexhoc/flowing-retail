package io.flowing.retail.order.mapper;

import io.flowing.retail.order.domain.Customer;
import io.flowing.retail.order.dto.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto toDto(Customer customer);
    Customer toModel(CustomerDto customerDto);
}