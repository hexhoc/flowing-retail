package io.flowing.retail.shippingservice.dto.mapper;

import io.flowing.retail.shippingservice.dto.WaybillDTO;
import io.flowing.retail.shippingservice.entity.Waybill;
import org.springframework.beans.BeanUtils;

import java.util.UUID;


public class WaybillMapper {
    public static WaybillDTO toDto(Waybill entity) {
        var dto = new WaybillDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(entity.getId().toString());
        return dto;
    }

    public static Waybill toEntity(WaybillDTO dto) {
        var entity = new Waybill();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(UUID.fromString(dto.getId()));
        return entity;
    }
}
