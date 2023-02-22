package io.flowing.retail.shippingservice.dto.mapper;

import io.flowing.retail.shippingservice.dto.WaybillDTO;
import io.flowing.retail.shippingservice.entity.Waybill;
import org.springframework.beans.BeanUtils;


public class WaybillMapper {
    public static WaybillDTO toDto(Waybill entity) {
        var dto = new WaybillDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Waybill toEntity(WaybillDTO dto) {
        var entity = new Waybill();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
