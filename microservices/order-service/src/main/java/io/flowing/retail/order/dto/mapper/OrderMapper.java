package io.flowing.retail.order.dto.mapper;

import io.flowing.retail.order.dto.OrderDTO;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDTO toDto(Order entity) {
        var dto = new OrderDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(entity.getId());
        dto.setOrderItems(entity.getOrderItems().stream().map(OrderItemMapper::toDto).collect(Collectors.toSet()));
        dto.setTotalPrice(entity.getTotalSum());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

    public static Order toEntity(OrderDTO dto) {
        var entity = new Order();
        BeanUtils.copyProperties(dto, entity, "version","createdDate","modifiedDate");
        entity.setOrderItems(dto.getOrderItems().stream()
                .map(orderItemDTO -> {
                    var orderItem = OrderItemMapper.toEntity(orderItemDTO);
                    orderItem.setOrder(entity);
                    return orderItem;
                })
                .collect(Collectors.toSet()));
        return entity;
    }

}
