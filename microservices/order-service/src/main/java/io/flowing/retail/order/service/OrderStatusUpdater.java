package io.flowing.retail.order.service;

import java.util.UUID;
import java.util.stream.Collectors;

import io.flowing.retail.order.config.KafkaConfig;
import io.flowing.retail.order.dto.CustomerDTO;
import io.flowing.retail.order.dto.InventoryItemDTO;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.entity.enums.OrderStatusEnum;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.messages.MessageSender;
import io.flowing.retail.order.messages.command.FetchGoodsCommandPayload;
import io.flowing.retail.order.messages.command.RetrievePaymentCommandPayload;
import io.flowing.retail.order.messages.command.ShipGoodsCommandPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class OrderStatusUpdater {
    private final MessageSender messageSender;
    private final CustomerService customerService;
    private final StatusChangeRuleSet<OrderStatusEnum, Order> statusRules =
            StatusChangeRuleSet
                    .builder(
                            this::updateStatusField,
                            () -> new RuntimeException("Не удалось поменять статус."))
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

        messageSender.send(
                new Message<>(
                        "RetrievePaymentCommand",
                        traceId,
                        new RetrievePaymentCommandPayload()
                                .setRefId(order.getId())
                                .setCustomerId(order.getCustomerId())
                                .setReason("order")
                                // TODO: amount is empty
                                .setAmount(order.getTotalSum().doubleValue()))
                        .setCorrelationid(correlationId),
                KafkaConfig.PAYMENT_TOPIC);
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

        messageSender.send(new Message<>(
                                   "FetchGoodsCommand",
                                   traceId,
                                   new FetchGoodsCommandPayload()
                                           .setRefId(order.getId())
                                           // TODO: items is empty
                                           .setItems(order.getOrderItems().stream()
                                                             .map(i -> new InventoryItemDTO(i.getProductId(), i.getQuantity()))
                                                             .collect(Collectors.toSet())))
                                   .setCorrelationid(correlationId),
                           KafkaConfig.INVENTORY_TOPIC);
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

        CustomerDTO customerDTO = customerService.getCustomerById(order.getCustomerId());

        String traceId = order.getId();
        // generate an UUID for this communication
        String correlationId = UUID.randomUUID().toString();

        messageSender.send(new Message<>(
                                   "ShipGoodsCommand",
                                   traceId,
                                   new ShipGoodsCommandPayload()
                                           .setRefId(order.getId())
                                           .setRecipientName(String.format("%s %s", customerDTO.getFirstName(), customerDTO.getLastName()))
                                           .setRecipientAddress(order.getAddress())
                                           .setLogisticsProvider("DHL"))
                                   .setCorrelationid(correlationId),
                           KafkaConfig.SHIPMENT_TOPIC);
    }

    private void allocatedCancelledAction(Order order) {
        // TODO implements
        log.info("ALLOCATED CANCELLED");
    }

    private void deliveredAction(Order order) {
        log.info("Order is delivered");
    }
}
