package io.flowing.retail.order.config;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableZeebeClient
@ZeebeDeployment(resources = "classpath:order-kafka.bpmn")
public class CamundaConfig {
}
