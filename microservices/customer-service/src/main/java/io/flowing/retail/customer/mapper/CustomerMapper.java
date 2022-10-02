package io.flowing.retail.customer.mapper;

import io.flowing.retail.customer.dto.CustomerDto;
import io.flowing.retail.customer.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerDto toDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setAddress(customer.getAddress());
        customerDto.setVersion(customer.getVersion());
        customerDto.setCreatedDate(customer.getCreatedDate());
        customerDto.setUpdatedDate(customer.getUpdatedDate());
        return customerDto;
    }

    public Customer toModel(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.getId());
        customer.setName(customerDto.getName());
        customer.setAddress(customerDto.getAddress());
        customer.setVersion(customerDto.getVersion());
        customer.setCreatedDate(customerDto.getCreatedDate());
        customer.setUpdatedDate(customerDto.getUpdatedDate());

        return customer;
    }
}
