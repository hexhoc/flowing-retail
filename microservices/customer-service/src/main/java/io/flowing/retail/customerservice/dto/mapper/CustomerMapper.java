package io.flowing.retail.customerservice.dto.mapper;

import io.flowing.retail.customerservice.dto.CustomerDTO;
import io.flowing.retail.customerservice.entity.Customer;
import org.springframework.beans.BeanUtils;

public class CustomerMapper {
    public static CustomerDTO toDto(Customer entity) {
        var dto = new CustomerDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Customer toEntity(CustomerDTO dto) {
        var entity = new Customer();
        BeanUtils.copyProperties(dto, entity,"version","createdDate","modifiedDate");
        return entity;
    }
}
