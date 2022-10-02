package io.flowing.retail.order.mapper;

import io.flowing.retail.order.dto.OrderItemDto;
import io.flowing.retail.order.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItemDto toDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setArticleId(orderItem.getArticleId());
        orderItemDto.setAmount(orderItem.getAmount());
        return orderItemDto;
    }

    public OrderItem toModel(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setArticleId(orderItemDto.getArticleId());
        orderItem.setAmount(orderItemDto.getAmount());
        return orderItem;
    }

}
