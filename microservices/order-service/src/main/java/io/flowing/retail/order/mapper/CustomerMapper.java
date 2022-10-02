package io.flowing.retail.order.mapper;

import io.flowing.retail.order.dto.CustomerDto;
import io.flowing.retail.order.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerDto toDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(customer.getName());
        customerDto.setAddress(customer.getAddress());
        customerDto.setVersion(customer.getVersion());
        customerDto.setCreatedDate(customer.getCreatedDate());
        customerDto.setUpdatedDate(customer.getUpdatedDate());
        return customerDto;
    }

    public Customer toModel(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setAddress(customerDto.getAddress());

        return customer;
    }
}
