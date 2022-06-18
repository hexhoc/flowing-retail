package io.flowing.retail.order.mapper;

import io.flowing.retail.order.domain.OrderItem;
import io.flowing.retail.order.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);
    OrderItem  toModel(OrderItemDto orderItemDto);
    List<OrderItemDto> toDtoList(List<OrderItem> orderItemList);
    List<OrderItem> toModelList(List<OrderItemDto> orderItemList);
}
