package io.flowing.retail.order.entity.enums;

public enum OrderEventEnum {
    CANCEL_ORDER,

    // PAYMENT
    PAYMENT_ORDER,
    PAYMENT_PASSED,
    PAYMENT_FAILED,

    // ALLOCATION
    ALLOCATE_ORDER,
    ALLOCATION_SUCCESS,
    ALLOCATION_FAILED,

    // TODO not used
    ALLOCATION_NO_INVENTORY,

    // SHIPPING
    ORDER_PICKED_UP
    }
