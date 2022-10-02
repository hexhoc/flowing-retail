package io.flowing.retail.customer.service;

import io.flowing.retail.customer.dto.CustomerDto;
import io.flowing.retail.customer.entity.Customer;
import io.flowing.retail.customer.mapper.CustomerMapper;
import io.flowing.retail.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String save(CustomerDto customerDto) {
        Customer customer = customerRepository.save(customerMapper.toModel(customerDto));
        return customer.getId().toString();
    }

    public void delete(String id) {
        customerRepository.deleteById(UUID.fromString(id));
    }

    public void update(String id, CustomerDto customerDto) {
        Customer currentCustomer = requireOne(id);
        Customer updateCustomer = customerMapper.toModel(customerDto);
        BeanUtils.copyProperties(updateCustomer, currentCustomer, "id");
        customerRepository.save(currentCustomer);
    }

    public CustomerDto getById(String id) {
        Customer original = requireOne(id);
        return customerMapper.toDto(original);
    }

    public Page<CustomerDto> getAllByPage(Pageable pageRequest) {
        List<CustomerDto> allItems = customerRepository.findAll(pageRequest).stream()
                .map(customerMapper::toDto)
                .toList();

        return new PageImpl<>(allItems);
    }

    private Customer requireOne(String id) {
        return customerRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
