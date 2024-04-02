package io.flowingretail.orderservice.service;

import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.messages.Message;
import io.flowingretail.common.messages.MessageSender;
import io.flowingretail.common.messages.command.FetchGoodsCommandPayload;
import io.flowingretail.common.messages.command.RetrievePaymentCommandPayload;
import io.flowingretail.common.messages.command.ShipGoodsCommandPayload;
import io.flowingretail.orderservice.adapter.CustomerRestClient;
import io.flowingretail.orderservice.dto.CustomerDto;
import io.flowingretail.common.dto.InventoryItemDTO;
import io.flowingretail.orderservice.entity.Order;
import io.flowingretail.orderservice.entity.enums.OrderStatusEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusUpdater {
    private final MessageSender messageSender;
    private final CustomerRestClient customerRestClient;
    private final StatusChangeRuleSet<OrderStatusEnum, Order> statusRules =
            StatusChangeRuleSet
                    .builder(
                            this::updateStatusField,
                            () -> new RuntimeException("Не удалось поменять статус."))
                // TODO: убрать отправку сообщения в action
                    // Движение вперед по статусам
                    .rule(OrderStatusEnum.NEW, OrderStatusEnum.PENDING_PAYMENT, this::pendingPaymentAction)
                    .rule(OrderStatusEnum.PENDING_PAYMENT, OrderStatusEnum.PENDING_ALLOCATION, this::pendingAllocationAction)
                    .rule(OrderStatusEnum.PENDING_PAYMENT, OrderStatusEnum.CANCELLED, this::pendingPaymentCancelledAction)
                    .rule(OrderStatusEnum.PENDING_ALLOCATION, OrderStatusEnum.PICKED_UP, this::pickedUpAction)
                    .rule(OrderStatusEnum.PENDING_ALLOCATION, OrderStatusEnum.CANCELLED, this::goodsFetchedCancelledAction)
                    .rule(OrderStatusEnum.PICKED_UP, OrderStatusEnum.DELIVERED, this::deliveredAction)
                    .build();


    /**
     * Смена статуса.
     *
     * @param order
     *         Заказ
     * @param newStatus
     *         Новый статус.
     *
     * @return Обновленный бункер.
     */
    public Order changeStatus(Order order, OrderStatusEnum newStatus) {
        return statusRules.change(order, order.getStatus(), newStatus);
    }

    /**
     * Принудительная смена статуса.
     *
     * @param order
     *         Заказ.
     * @param newStatus
     *         Новый статус.
     *
     * @return Обновленный заказ.
     */
    public Order forceChangeStatus(Order order, OrderStatusEnum newStatus) {
        return statusRules.forceChange(order, newStatus);
    }

    private void updateStatusField(Order order, OrderStatusEnum status) {
        order.setStatus(status);
    }

    /**
     * Step 1. Send message about order to payment-service through kafka
     * To receive payment confirmation
     */
    private void pendingPaymentAction(Order order) {
        log.info("retrieve-payment job");

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
            .type("RetrievePaymentCommand")
            .data(retrievePaymentCommand)
            .source("order-service")
            .time(LocalDateTime.now())
            .traceid(traceId)
            .correlationid(correlationId)
            .build();

        messageSender.send(responseMessage, KafkaConfig.PAYMENT_TOPIC);
    }

    private void pendingPaymentCancelledAction(Order order) {
        // TODO implements
        log.info("PAYMENT CANCELLED");
    }

    /**
     * Step 2. Send message to inventory-service through kafka
     * To check the balance of goods in stock
     */
    private void pendingAllocationAction(Order order) {
        log.info("fetch-goods job");

        String traceId = order.getId();
        // generate an UUID for this communication
        String correlationId = UUID.randomUUID().toString();

        var fetchGoodsCommandPayload = new FetchGoodsCommandPayload()
            .setRefId(order.getId())
            .setItems(order.getOrderItems().stream()
                .map(i -> new InventoryItemDTO(i.getProductId(), i.getQuantity()))
                .collect(Collectors.toSet()));

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type("FetchGoodsCommand")
            .data(fetchGoodsCommandPayload)
            .source("order-service")
            .time(LocalDateTime.now())
            .traceid(traceId)
            .correlationid(correlationId)
            .build();

        messageSender.send(responseMessage, KafkaConfig.INVENTORY_TOPIC);
    }

    private void goodsFetchedCancelledAction(Order order) {
        // TODO implements
        log.info("PENDING ALLOCATION CANCELLED");
    }

    /**
     * Step 3. Send message to shipping-service through kafka
     * To prepare the goods for shipment to the buyer
     *
     */
    private void pickedUpAction(Order order) {
        log.info("ship-goods job");

        CustomerDto customerDTO = customerRestClient.getCustomerById(order.getCustomerId());

        String traceId = order.getId();
        // generate an UUID for this communication
        String correlationId = UUID.randomUUID().toString();

        var shipGoodsCommandPayload = new ShipGoodsCommandPayload()
            .setRefId(order.getId())
            .setRecipientName(String.format("%s %s", customerDTO.getFirstName(), customerDTO.getLastName()))
            .setRecipientAddress(order.getAddress())
            .setLogisticsProvider("DHL");

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type("ShipGoodsCommand")
            .data(shipGoodsCommandPayload)
            .source("order-service")
            .time(LocalDateTime.now())
            .traceid(traceId)
            .correlationid(correlationId)
            .build();

        messageSender.send(responseMessage, KafkaConfig.SHIPMENT_TOPIC);
    }

    private void allocatedCancelledAction(Order order) {
        // TODO implements
        log.info("ALLOCATED CANCELLED");
    }

    private void deliveredAction(Order order) {
        log.info("Order is delivered");
    }
}
