package io.flowingretail.orderservice.service;

import static io.flowingretail.common.constants.EventTypeConstants.RETRIEVE_PAYMENT_COMMAND;
import static io.flowingretail.common.constants.ServiceNameConstants.ORDER_SERVICE;

import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.messages.Message;
import io.flowingretail.common.messages.MessageSender;
import io.flowingretail.common.messages.command.RetrievePaymentCommandPayload;
import io.flowingretail.orderservice.dto.OrderDTO;
import io.flowingretail.orderservice.dto.mapper.OrderMapper;
import io.flowingretail.orderservice.entity.Order;
import io.flowingretail.orderservice.entity.enums.OrderStatusEnum;
import io.flowingretail.orderservice.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderStatusUpdater orderStatusUpdater;
    private final OrderMapper orderMapper;
    private final MessageSender messageSender;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDto) {
        // persist domain entity
        // (if we want to do this "transactional" this could be a step in the workflow)
        Order entity = orderMapper.toEntity(orderDto);
        entity.setStatus(OrderStatusEnum.NEW);
        entity = orderRepository.save(entity);
        log.info("Save order " + entity.getId());

        orderStatusUpdater.changeStatus(entity, entity.getStatus().nextState());

        sendRetrievePaymentCommand(entity);

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

    private void sendRetrievePaymentCommand(Order order) {
        String traceId = order.getId();
        // generate an UUID for this communication
        String correlationId = UUID.randomUUID().toString();

        var retrievePaymentCommand = new RetrievePaymentCommandPayload()
            .setRefId(order.getId())
            .setCustomerId(order.getCustomerId())
            .setReason("order")
            // TODO: amount is empty
            .setAmount(order.getTotalSum().doubleValue());

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type(RETRIEVE_PAYMENT_COMMAND)
            .data(retrievePaymentCommand)
            .source(ORDER_SERVICE)
            .time(LocalDateTime.now())
            .traceid(traceId)
            .correlationid(correlationId)
            .build();

        messageSender.send(responseMessage, KafkaConfig.PAYMENT_TOPIC);

    }

}
