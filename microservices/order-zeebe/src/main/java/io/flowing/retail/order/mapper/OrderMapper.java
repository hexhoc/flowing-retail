package io.flowing.retail.order.mapper;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, OrderItemMapper.class})
public interface OrderMapper {
    OrderDto toDto(Order order);
    @Mapping(target = "id", ignore = true)
    Order toModel(OrderDto orderDto);
}