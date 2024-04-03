package io.flowingretail.orderservice.service;

import io.flowingretail.orderservice.entity.Order;
import io.flowingretail.orderservice.entity.enums.OrderStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusUpdater {

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
    }

    private void allocatedCancelledAction(Order order) {
        // TODO implements
        log.info("ALLOCATED CANCELLED");
    }

    private void deliveredAction(Order order) {
        log.info("Order is delivered");
    }
}
