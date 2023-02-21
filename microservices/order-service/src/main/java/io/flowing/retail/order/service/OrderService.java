package io.flowing.retail.order.service;

import io.flowing.retail.order.dto.OrderDTO;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.dto.mapper.OrderMapper;
import io.flowing.retail.order.entity.enums.OrderStatusEnum;
import io.flowing.retail.order.process.OrderKafkaProcess;
import io.flowing.retail.order.repository.OrderRepository;
import lombok.extern.java.Log;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderKafkaProcess orderKafkaProcess;

    public OrderService(
            OrderRepository orderRepository,
            // use lazy to resolve circular Dependencies
            @Lazy OrderKafkaProcess orderKafkaProcess) {
        this.orderRepository = orderRepository;
        this.orderKafkaProcess = orderKafkaProcess;
    }

    public OrderDTO createOrder(OrderDTO orderDto) {
        // persist domain entity
        // (if we want to do this "transactional" this could be a step in the workflow)
        Order newOrder = orderRepository.save(OrderMapper.toEntity(orderDto));
        log.info("Save order " + newOrder.getId());
        orderKafkaProcess.startProcess(newOrder.getId());
        return OrderMapper.toDto(newOrder);
    }

    @Transactional
    public OrderDTO updateOrder(String id, OrderDTO orderDto) {
        Order entity = requireOne(id);
        Order updatedEntity = OrderMapper.toEntity(orderDto);
        BeanUtils.copyProperties(updatedEntity, entity, "id","version","createdDate","modifiedDate");
        return OrderMapper.toDto(orderRepository.save(entity));
    }

    public Page<OrderDTO> findAll(Integer customerId, Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);

        Page<Order> entityPage;
        if (Objects.nonNull(customerId)) {
            entityPage = orderRepository.findAllByCustomerId(customerId, pageRequest);
        } else {
            entityPage = orderRepository.findAll(pageRequest);
        }

        List<OrderDTO> dtoList = entityPage.stream()
                .map(OrderMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

    public OrderDTO findById(String id) {
        return OrderMapper.toDto(requireOne(id));
    }

    private Order requireOne(String id) {
        return orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

    public void deleteById(String id) {
        orderRepository.deleteById(UUID.fromString(id));
    }

    @Transactional
    public void updateStatus(String id, OrderStatusEnum orderStatus) {
        Order order = orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
        order.setStatus(orderStatus);

        orderRepository.save(order);
    }
}
