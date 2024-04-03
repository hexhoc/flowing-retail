package io.flowingretail.shippingservice.dto.mapper;

import io.flowingretail.shippingservice.dto.WaybillDto;
import io.flowingretail.shippingservice.entity.Waybill;
import java.util.UUID;
import org.springframework.beans.BeanUtils;


public class WaybillMapper {
    public static WaybillDto toDto(Waybill entity) {
        var dto = new WaybillDto();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(entity.getId().toString());
        return dto;
    }

    public static Waybill toEntity(WaybillDto dto) {
        var entity = new Waybill();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(UUID.fromString(dto.getId()));
        return entity;
    }
}
