package io.flowing.retail.paymentservice.dto.mapper;

import io.flowing.retail.paymentservice.dto.PaymentDTO;
import io.flowing.retail.paymentservice.entity.Payment;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public class PaymentMapper {
    public static PaymentDTO toDto(Payment entity) {
        var dto = new PaymentDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setId(entity.getId().toString());
        return dto;
    }

    public static Payment toEntity(PaymentDTO dto) {
        var entity = new Payment();
        BeanUtils.copyProperties(dto, entity,"version","createdDate","modifiedDate");
        entity.setId(UUID.fromString(dto.getId()));
        return entity;
    }
}
