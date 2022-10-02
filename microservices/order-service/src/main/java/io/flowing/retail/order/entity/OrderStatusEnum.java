package io.flowing.retail.order.entity;

public enum OrderStatusEnum {
    NEW, CANCELLED, PAYMENT_AWAITING, PAID, PAYMENT_EXCEPTION,
    ALLOCATION_AWAITING, ALLOCATED, ALLOCATION_ERROR, INVENTORY_AWAITING,
    PICKED_UP, DELIVERED, DELIVERY_EXCEPTION

}