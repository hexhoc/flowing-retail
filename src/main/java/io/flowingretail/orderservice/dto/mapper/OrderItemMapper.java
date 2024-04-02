package io.flowingretail.orderservice.dto.mapper;

import io.flowingretail.orderservice.dto.OrderItemDTO;
import io.flowingretail.orderservice.entity.OrderItem;
import java.util.Objects;
import java.util.UUID;
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

