package io.flowingretail.customerservice.service;

import io.flowingretail.customerservice.dto.CustomerDto;
import io.flowingretail.customerservice.dto.mapper.CustomerMapper;
import io.flowingretail.customerservice.adapter.out.jpa.Customer;
import io.flowingretail.customerservice.adapter.out.jpa.CustomerRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public void delete(Integer id) {
        customerRepository.deleteById(id);
    }

    public CustomerDto update(Integer id, CustomerDto dto) {
        Customer entity = requireOne(id);
        Customer updatedEntity = CustomerMapper.toEntity(dto);
        BeanUtils.copyProperties(updatedEntity, entity ,"id", "version","createdDate","modifiedDate");
        return CustomerMapper.toDto(customerRepository.save(entity));
    }

    public Page<CustomerDto> getAll(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);

        Page<Customer> entityPage = customerRepository.findAll(pageRequest);
        List<CustomerDto> dtoList = entityPage.stream()
                .map(CustomerMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

    @Cacheable(value = "getById", key = "#id")
    public CustomerDto getById(Integer id) {
        return CustomerMapper.toDto(requireOne(id));
    }

    private Customer requireOne(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
