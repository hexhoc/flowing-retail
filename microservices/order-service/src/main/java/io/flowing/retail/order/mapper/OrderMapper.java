package io.flowing.retail.order.mapper;

import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "orderId", source = "id")
    OrderDto toDto(Order order);
    @Mapping(target = "id", source = "orderId")
    Order toModel(OrderDto orderDto);
}