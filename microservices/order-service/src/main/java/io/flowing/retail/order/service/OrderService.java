package io.flowing.retail.order.service;

import io.camunda.zeebe.client.ZeebeClient;
import io.flowing.retail.order.dto.OrderDto;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.mapper.OrderMapper;
import io.flowing.retail.order.process.OrderKafkaProcess;
import io.flowing.retail.order.repository.OrderRepository;
import io.flowing.retail.order.process.OrderFlowContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderKafkaProcess orderKafkaProcess;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        // persist domain entity
        // (if we want to do this "transactional" this could be a step in the workflow)
        Order newOrder = orderRepository.save(orderMapper.toModel(orderDto));
        orderKafkaProcess.startProcess(newOrder.getId());
        return orderMapper.toDto(newOrder);
    }

    @Transactional
    public OrderDto updateOrder(OrderDto orderDto) {
        return orderMapper.toDto(orderRepository.save(orderMapper.toModel(orderDto)));
    }

    public Page<OrderDto> findAll(PageRequest pageRequest) {
        List<OrderDto> allItems = orderRepository.findAll(pageRequest).stream()
                .map(orderMapper::toDto)
                .toList();

        return new PageImpl<>(allItems);
    }
    public OrderDto findById(String id) {
        return orderMapper.toDto(requireOne(id));
    }

    private Order requireOne(String id) {
        return orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

    public void deleteById(String id) {
        orderRepository.deleteById(UUID.fromString(id));
    }
}
