package io.flowing.retail.order.service;

import io.flowing.retail.order.dto.CustomerDto;
import io.flowing.retail.order.model.CustomerPagedList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    public static final String CUSTOMER_PATH_V1 = "/api/v1/customer/";
    private final RestTemplate restTemplate;

    @Value("${custom.flowingretail.customer-service.host}")
    private String customerServiceHost;

    public CustomerService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public CustomerDto getCustomerById(UUID uuid){
        return restTemplate.getForObject(customerServiceHost + CUSTOMER_PATH_V1 + uuid.toString(), CustomerDto.class);
    }

    public CustomerPagedList getListOfCustomer() {
        return restTemplate.getForObject(customerServiceHost + CUSTOMER_PATH_V1, CustomerPagedList.class);
    }

}
