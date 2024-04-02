package io.flowingretail.cusomerservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.flowingretail.customerservice.dto.CustomerDto;
import io.flowingretail.customerservice.dto.mapper.CustomerMapper;
import io.flowingretail.customerservice.entity.Customer;
import io.flowingretail.customerservice.repository.CustomerRepository;
import io.flowingretail.customerservice.service.CustomerService;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class) // Needed to use @Mock and @InjectMocks
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        // customerRepository = Mockito.mock(CustomerRepository.class);
        // customerService = new CustomerService(customerRepository);
        mockCustomer = new Customer();
        mockCustomer.setId(1);
        mockCustomer.setFirstName("John");
        mockCustomer.setLastName("Smith");
        mockCustomer.setEmail("john_smith@mail.com");
        mockCustomer.setPhone("79161234567");
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("test for delete method")
    @Test
    void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        // given - precondition or setup
        int customerId = 1;

        willDoNothing().given(customerRepository).deleteById(customerId);

        // when -  action or the behaviour that we are going test
        customerService.delete(customerId);

        // then - verify the output
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @DisplayName("test for update method")
    @Test
    void givenCustomerObject_whenUpdateCustomer_thenReturnUpdatedCustomer(){
        // given - precondition or setup
        given(customerRepository.findById(1)).willReturn(Optional.of(mockCustomer));
        given(customerRepository.save(mockCustomer)).willReturn(mockCustomer);
        mockCustomer.setEmail("ram@gmail.com");
        mockCustomer.setFirstName("Ram");
        // when -  action or the behaviour that we are going test
        CustomerDto updatedCustomer = customerService.update(mockCustomer.getId(), CustomerMapper.toDto(mockCustomer));

        // then - verify the output
        assertThat(updatedCustomer.getEmail()).isEqualTo("ram@gmail.com");
        assertThat(updatedCustomer.getFirstName()).isEqualTo("Ram");
    }

    @DisplayName("test for getAll method")
    @Test
    void givenCustomerList_whenGetAllCustomer_thenReturnCustomerList() {
        // given - precondition or setup
        Customer mockCustomer2 = new Customer();
        BeanUtils.copyProperties(mockCustomer, mockCustomer2);
        mockCustomer2.setId(2);

        given(customerRepository.findAll(PageRequest.of(0,2)))
                .willReturn(new PageImpl<>(List.of(mockCustomer,mockCustomer2)));

        // when -  action or the behaviour that we are going test
        Page<CustomerDto> customerList = customerService.getAll(0,2);

        // then - verify the output
        assertThat(customerList).isNotNull();
        assertThat(customerList.getTotalElements()).isEqualTo(2);
    }

    @DisplayName("test for getAll method (negative scenario)")
    @Test
    void givenEmptyCustomerList_whenGetAllCustomer_thenReturnEmptyCustomerList() {
        given(customerRepository.findAll(PageRequest.of(0,2)))
                .willReturn(new PageImpl<>(Collections.emptyList()));

        // when -  action or the behaviour that we are going test
        Page<CustomerDto> customerList = customerService.getAll(0,2);

        // then - verify the output
        assertThat(customerList).isEmpty();
    }

    @DisplayName("test for getById method")
    @Test
    void getById() {
        // given
        given(customerRepository.findById(1)).willReturn(Optional.of(mockCustomer));
        // when
        CustomerDto customerDto = customerService.getById(mockCustomer.getId());
        // then
        assertThat(customerDto).isNotNull();
    }

    @DisplayName("test for getById method with exception")
    @Test
    void getByIdWithException() {
        // given
        given(customerRepository.findById(1)).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        assertThrows(NoSuchElementException.class, () -> {
            customerService.getById(1);
        });

        // then
        verify(customerRepository, never()).getById(1);
    }
}