package io.flowing.retail.order.mapper;

import io.flowing.retail.order.entity.Customer;
import io.flowing.retail.order.dto.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto toDto(Customer customer);
    Customer toModel(CustomerDto customerDto);
}