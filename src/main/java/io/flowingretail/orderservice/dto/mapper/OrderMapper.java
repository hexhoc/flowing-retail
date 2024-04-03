package io.flowingretail.orderservice.dto.mapper;

import io.flowingretail.orderservice.dto.OrderDto;
import io.flowingretail.orderservice.entity.Order;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    /**
     * Map all order entity properties to order command dto
     * @param entity order entity
     * @return order dto
     */
    public OrderDto toDTO(Order entity) {
        var dto = new OrderDto();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(entity.getId());
        dto.setOrderItems(entity.getOrderItems().stream().map(orderItemMapper::toDto).collect(Collectors.toSet()));
        dto.setTotalPrice(entity.getTotalSum());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

    /**
     * Map all order dto properties to order entity exclude ("version","createdDate","modifiedDate")
     * @param dto order dto
     * @return order entity
     */
    public Order toEntity(OrderDto dto) {
        var entity = new Order();
        BeanUtils.copyProperties(dto, entity, "version","createdDate","modifiedDate");
//        entity.setStatus(OrderStatusEnum.valueOf(dto.getStatus()));
        entity.setOrderItems(dto.getOrderItems().stream()
                .map(orderItemDTO -> {
                    var orderItem = orderItemMapper.toEntity(orderItemDTO);
                    orderItem.setOrder(entity);
                    return orderItem;
                })
                .collect(Collectors.toSet()));
        return entity;
    }

}
