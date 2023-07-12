package io.flowing.retail.order.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import io.flowing.retail.order.dto.OrderDTO;
import io.flowing.retail.order.dto.mapper.OrderMapper;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.entity.enums.OrderStatusEnum;
import io.flowing.retail.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderStatusUpdater orderStatusUpdater;
    private final OrderMapper orderMapper;

    public OrderDTO createOrder(OrderDTO orderDto) {
        // persist domain entity
        // (if we want to do this "transactional" this could be a step in the workflow)
        Order entity = orderMapper.toEntity(orderDto);
        entity.setStatus(OrderStatusEnum.NEW);
        entity = orderRepository.save(entity);
        log.info("Save order " + entity.getId());

        orderStatusUpdater.changeStatus(entity, entity.getStatus().nextState());

        return orderMapper.toDTO(entity);
    }

    @Transactional
    public OrderDTO updateOrder(String id, OrderDTO orderDto) {
        var orderOptional = orderRepository.findById(UUID.fromString(id));
        if (orderOptional.isPresent()) {
//            var currentEntity = orderOptional.get();
            var updatedEntity = orderMapper.toEntity(orderDto);
//            //TODO: Возможно и не нужно заполнять эти поля
//            updatedEntity.setVersion(currentEntity.getVersion());
//            updatedEntity.setCreatedDate(currentEntity.getCreatedDate());
//            updatedEntity.setModifiedDate(currentEntity.getModifiedDate());

            return orderMapper.toDTO(orderRepository.save(updatedEntity));
        } else {
            throw new NoSuchElementException("Resource not found: " + id);
        }
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

    @Transactional
    public void changeStatus(String id) {
        Order order = orderRepository.findById(UUID.fromString(id))
                                     .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
        orderStatusUpdater.changeStatus(order, order.getStatus().nextState());

//        @Transactional save entity automatically
//        orderRepository.save(order);
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
                                                .map(orderMapper::toDTO)
                                                .toList();

        return new PageImpl<>(dtoList);
    }

    public OrderDTO findById(String id) {
        return orderMapper.toDTO(requireOne(id));
    }

    private Order requireOne(String id) {
        return orderRepository.findById(UUID.fromString(id))
                                   .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

}
