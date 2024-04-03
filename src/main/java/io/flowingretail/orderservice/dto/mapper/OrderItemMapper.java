package io.flowingretail.orderservice.dto.mapper;

import io.flowingretail.orderservice.dto.OrderItemDto;
import io.flowingretail.orderservice.entity.OrderItem;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemDto toDto(OrderItem entity) {
        var dto = new OrderItemDto();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(Objects.requireNonNullElse(entity.getId(),"").toString());
        return dto;
    }

    public OrderItem toEntity(OrderItemDto dto) {
        var entity = new OrderItem();
        BeanUtils.copyProperties(dto, entity);
        if (Objects.nonNull(dto.getId())) {
            entity.setId(UUID.fromString(dto.getId()));
        }
        return entity;
    }
}

