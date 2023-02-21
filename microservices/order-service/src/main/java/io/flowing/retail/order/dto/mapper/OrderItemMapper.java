package io.flowing.retail.order.dto.mapper;

import io.flowing.retail.order.dto.OrderItemDTO;
import io.flowing.retail.order.entity.OrderItem;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

public class OrderItemMapper {
    public static OrderItemDTO toDto(OrderItem entity) {
        var dto = new OrderItemDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static OrderItem toEntity(OrderItemDTO dto) {
        var entity = new OrderItem();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}

