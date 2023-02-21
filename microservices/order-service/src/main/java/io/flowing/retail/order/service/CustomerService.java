package io.flowing.retail.order.service;

import io.flowing.retail.order.dto.CustomerDTO;
import io.flowing.retail.order.dto.CustomerPagedList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerService {

    public static final String CUSTOMER_PATH_V1 = "/api/v1/customer/";
    private final RestTemplate restTemplate;

    @Value("${custom.flowingretail.customer-service.host}")
    private String customerServiceHost;

    public CustomerService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public CustomerDTO getCustomerById(Integer id){
        return restTemplate.getForObject(customerServiceHost + CUSTOMER_PATH_V1 + id, CustomerDTO.class);
    }

    public CustomerPagedList getListOfCustomer() {
        return restTemplate.getForObject(customerServiceHost + CUSTOMER_PATH_V1, CustomerPagedList.class);
    }

}
