package io.flowingretail.paymentservice.dto.mapper;

import io.flowingretail.paymentservice.dto.PaymentDto;
import io.flowingretail.paymentservice.adapter.out.jpa.Payment;
import java.util.UUID;
import org.springframework.beans.BeanUtils;

public class PaymentMapper {
    public static PaymentDto toDto(Payment entity) {
        var dto = new PaymentDto();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(entity.getId().toString());
        return dto;
    }

    public static Payment toEntity(PaymentDto dto) {
        var entity = new Payment();
        BeanUtils.copyProperties(dto, entity,"version","createdDate","modifiedDate");
        entity.setId(UUID.fromString(dto.getId()));
        return entity;
    }
}
