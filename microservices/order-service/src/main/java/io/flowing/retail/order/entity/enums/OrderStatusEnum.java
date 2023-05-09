package io.flowing.retail.order.entity.enums;

public enum OrderStatusEnum {

    // COMMON
    NEW {
        @Override
        public OrderStatusEnum nextState() {
            return PENDING_PAYMENT;
        }
    },
    CANCELLED {
        @Override
        public OrderStatusEnum nextState() {
            return this;
        }
    },

    // PAYMENT
    PENDING_PAYMENT {
        @Override
        public OrderStatusEnum nextState() {
            return PENDING_ALLOCATION;
        }
    },

    // ALLOCATION
    PENDING_ALLOCATION {
        @Override
        public OrderStatusEnum nextState() {
            return PICKED_UP;
        }
    },

    // SHIPPING
    PICKED_UP {
        @Override
        public OrderStatusEnum nextState() {
            return DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public OrderStatusEnum nextState() {
            return this;
        }
    };

    public abstract OrderStatusEnum nextState();
}
