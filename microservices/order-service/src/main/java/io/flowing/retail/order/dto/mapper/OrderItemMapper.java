package io.flowing.retail.order.dto.mapper;

import java.util.Objects;
import java.util.UUID;

import io.flowing.retail.order.dto.OrderItemDTO;
import io.flowing.retail.order.entity.OrderItem;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemDTO toDto(OrderItem entity) {
        var dto = new OrderItemDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(Objects.requireNonNullElse(entity.getId(),"").toString());
        return dto;
    }

    public OrderItem toEntity(OrderItemDTO dto) {
        var entity = new OrderItem();
        BeanUtils.copyProperties(dto, entity);
        if (Objects.nonNull(dto.getId())) {
            entity.setId(UUID.fromString(dto.getId()));
        }
        return entity;
    }
}

