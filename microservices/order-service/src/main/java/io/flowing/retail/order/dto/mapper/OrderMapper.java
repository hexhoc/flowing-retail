package io.flowing.retail.order.dto.mapper;

import io.flowing.retail.order.dto.OrderDTO;
import io.flowing.retail.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    /**
     * Map all order entity properties to order command dto
     * @param entity order entity
     * @return order dto
     */
    public OrderDTO toDTO(Order entity) {
        var dto = new OrderDTO();
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
    public Order toEntity(OrderDTO dto) {
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
