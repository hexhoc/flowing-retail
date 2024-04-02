package io.flowingretail.orderservice.adapter;

import io.flowingretail.orderservice.dto.CustomerDto;
import io.flowingretail.orderservice.dto.CustomerPagedList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerRestClient {

    public static final String CUSTOMER_PATH_V1 = "/api/v1/customer/";
    private final RestTemplate restTemplate;

    @Value("${custom.flowingretail.customer-service.host}")
    private String customerServiceHost;

    public CustomerRestClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public CustomerDto getCustomerById(Integer id){
        return restTemplate.getForObject(customerServiceHost + CUSTOMER_PATH_V1 + id, CustomerDto.class);
    }

    public CustomerPagedList getListOfCustomer() {
        return restTemplate.getForObject(customerServiceHost + CUSTOMER_PATH_V1, CustomerPagedList.class);
    }

}
