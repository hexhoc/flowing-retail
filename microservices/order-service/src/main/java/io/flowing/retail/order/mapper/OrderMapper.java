package io.flowing.retail.order.mapper;

import io.flowing.retail.order.dto.OrderDto;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final CustomerService customerService;
    private final OrderItemMapper orderItemMapper;

    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setCustomer(this.customerService.getCustomerById(order.getCustomerId()));
        orderDto.setOrderStatus(order.getOrderStatus().toString());
        orderDto.setItems(order.getItems().stream().map(orderItemMapper::toDto).toList());
        orderDto.setTotalSum(order.getTotalSum());
        return orderDto;
    }

    public Order toModel(OrderDto orderDto) {

        Order order = new Order();
        order.setId(orderDto.getOrderId());
        order.setCustomerId(orderDto.getCustomer().getId());
        order.setItems(orderDto.getItems().stream().map(orderItemMapper::toModel).toList());
        // set order to each order line
        order.getItems().forEach(orderItem -> orderItem.setOrder(order));

        return order;
    }

}
