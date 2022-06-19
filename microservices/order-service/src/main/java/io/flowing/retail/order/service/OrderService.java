package io.flowing.retail.order.service;

import io.camunda.zeebe.client.ZeebeClient;
import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.persistence.OrderRepository;
import io.flowing.retail.order.process.OrderFlowContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ZeebeClient zeebeClient;

    @Transactional
    public Order createOrder(Order order) {
        // persist domain entity
        // (if we want to do this "transactional" this could be a step in the workflow)
        Order newOrder = orderRepository.save(order);

        // prepare data for workflow
        OrderFlowContext context = new OrderFlowContext();
        context.setOrderId(order.getId());
        context.setTraceId(UUID.randomUUID().toString());

        // and kick of a new flow instance
        System.out.println("New order placed, start flow with " + context);
        zeebeClient.newCreateInstanceCommand() //
                .bpmnProcessId("order-kafka") //
                .latestVersion() //
                .variables(context.asMap()) //
                .send().join();

        return newOrder;
    }
}
