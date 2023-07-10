package io.flowing.retail.order.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import io.flowing.retail.order.dto.OrderCommandDTO;
import io.flowing.retail.order.dto.OrderQueryDTO;
import io.flowing.retail.order.dto.mapper.OrderMapper;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.entity.enums.OrderStatusEnum;
import io.flowing.retail.order.repository.OrderCommandRepository;
import io.flowing.retail.order.repository.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderCommandService {
    private final OrderCommandRepository orderCommandRepository;
    private final OrderStatusUpdater orderStatusUpdater;
    private final OrderMapper orderMapper;

    public OrderQueryDTO createOrder(OrderCommandDTO orderCommandDto) {
        // persist domain entity
        // (if we want to do this "transactional" this could be a step in the workflow)
        Order entity = orderMapper.toEntity(orderCommandDto);
        entity.setStatus(OrderStatusEnum.NEW);
        entity = orderCommandRepository.save(entity);
        log.info("Save order " + entity.getId());

        orderStatusUpdater.changeStatus(entity, entity.getStatus().nextState());

        return orderMapper.toOrderQueryDTO(entity);
    }

    @Transactional
    public OrderQueryDTO updateOrder(String id, OrderCommandDTO orderCommandDto) {
        var orderOptional = orderCommandRepository.findById(UUID.fromString(id));
        if (orderOptional.isPresent()) {
            var currentEntity = orderOptional.get();
            var updatedEntity = orderMapper.toEntity(orderCommandDto);
            //TODO: Возможно и не нужно заполнять эти поля
            updatedEntity.setVersion(currentEntity.getVersion());
            updatedEntity.setCreatedDate(currentEntity.getCreatedDate());
            updatedEntity.setModifiedDate(currentEntity.getModifiedDate());

            return orderMapper.toOrderQueryDTO(orderCommandRepository.save(updatedEntity));
        } else {
            throw new NoSuchElementException("Resource not found: " + id);
        }
    }

    public void deleteById(String id) {
        orderCommandRepository.deleteById(UUID.fromString(id));
    }

    @Transactional
    public void updateStatus(String id, OrderStatusEnum orderStatus) {
        Order order = orderCommandRepository.findById(UUID.fromString(id))
                                            .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
        order.setStatus(orderStatus);

        orderCommandRepository.save(order);
    }

    @Transactional
    public void changeStatus(String id) {
        Order order = orderCommandRepository.findById(UUID.fromString(id))
                                            .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
        orderStatusUpdater.changeStatus(order, order.getStatus().nextState());

//        @Transactional save entity automatically
//        orderRepository.save(order);
    }
}
