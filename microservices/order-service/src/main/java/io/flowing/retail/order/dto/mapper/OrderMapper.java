package io.flowing.retail.order.dto.mapper;

import io.flowing.retail.order.dto.OrderCommandDTO;
import io.flowing.retail.order.dto.OrderQueryDTO;
import io.flowing.retail.order.entity.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    /**
     * Map all order entity properties to order query dto
     * @param entity order entity
     * @return order dto
     */
    public OrderQueryDTO toOrderQueryDTO(Order entity) {
        var dto = new OrderQueryDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(entity.getId());
        dto.setOrderItems(entity.getOrderItems().stream().map(OrderItemMapper::toDto).collect(Collectors.toSet()));
        dto.setTotalPrice(entity.getTotalSum());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

    /**
     * Map all order entity properties to order command dto
     * @param entity order entity
     * @return order dto
     */
    public OrderCommandDTO toOrderCommandDTO(Order entity) {
        var dto = new OrderCommandDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(entity.getId());
        dto.setOrderItems(entity.getOrderItems().stream().map(OrderItemMapper::toDto).collect(Collectors.toSet()));
        dto.setTotalPrice(entity.getTotalSum());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

    /**
     * Map all order dto properties to order entity exclude ("version","createdDate","modifiedDate")
     * @param dto order dto
     * @return order entity
     */
    public Order toEntity(OrderCommandDTO dto) {
        var entity = new Order();
        BeanUtils.copyProperties(dto, entity, "version","createdDate","modifiedDate");
//        entity.setStatus(OrderStatusEnum.valueOf(dto.getStatus()));
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
