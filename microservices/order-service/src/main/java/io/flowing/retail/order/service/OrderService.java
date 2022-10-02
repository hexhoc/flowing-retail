package io.flowing.retail.order.service;

import io.camunda.zeebe.client.ZeebeClient;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.process.OrderKafkaProcess;
import io.flowing.retail.order.repository.OrderRepository;
import io.flowing.retail.order.process.OrderFlowContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderKafkaProcess orderKafkaProcess;

    @Transactional
    public Order createOrder(Order order) {
        // persist domain entity
        // (if we want to do this "transactional" this could be a step in the workflow)
        Order newOrder = orderRepository.save(order);
        orderKafkaProcess.startProcess(order.getId());
        return newOrder;
    }

    @Transactional
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public Page<Order> findAll(PageRequest pageRequest) {
        return orderRepository.findAll(pageRequest);
    }
    public Order findById(String id) {
        return orderRepository.findById(UUID.fromString(id)).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void deleteById(String id) {
        orderRepository.deleteById(UUID.fromString(id));
    }
}
