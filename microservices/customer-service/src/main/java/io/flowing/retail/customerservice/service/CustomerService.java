package io.flowing.retail.customerservice.service;
import io.flowing.retail.customerservice.dto.CustomerDTO;
import io.flowing.retail.customerservice.dto.mapper.CustomerMapper;
import io.flowing.retail.customerservice.entity.Customer;
import io.flowing.retail.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public void delete(Integer id) {
        customerRepository.deleteById(id);
    }

    public void update(Integer id, CustomerDTO dto) {
        Customer entity = requireOne(id);
        Customer updatedEntity = CustomerMapper.toEntity(dto);
        BeanUtils.copyProperties(updatedEntity, entity ,"id", "version","createdDate","modifiedDate");
        customerRepository.save(entity);
    }

    public Page<CustomerDTO> getAll(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);

        Page<Customer> entityPage = customerRepository.findAll(pageRequest);
        List<CustomerDTO> dtoList = entityPage.stream()
                .map(CustomerMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

    @Cacheable(value = "getById", key = "#id")
    public CustomerDTO getById(Integer id) {
        return CustomerMapper.toDto(requireOne(id));
    }

    private Customer requireOne(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
