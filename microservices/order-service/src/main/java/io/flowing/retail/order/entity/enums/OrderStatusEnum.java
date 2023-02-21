package io.flowing.retail.order.entity.enums;

public enum OrderStatusEnum {
    NEW,
    TO_RESERVE,
    PAYMENT_AWAITING,
    SHIPMENT_READY,
    DONE,
    CANCELLED,
    PAYMENT_CANCELLATION,
    RESERVE_CANCELLATION
}
