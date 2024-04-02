package io.flowingretail.customerservice.dto.mapper;

import io.flowingretail.customerservice.dto.CustomerDto;
import io.flowingretail.customerservice.entity.Customer;
import org.springframework.beans.BeanUtils;

public class CustomerMapper {
    public static CustomerDto toDto(Customer entity) {
        var dto = new CustomerDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Customer toEntity(CustomerDto dto) {
        var entity = new Customer();
        BeanUtils.copyProperties(dto, entity,"version","createdDate","modifiedDate");
        return entity;
    }
}
