package io.flowingretail.paymentservice.dto.mapper;

import io.flowingretail.paymentservice.dto.PaymentDTO;
import io.flowingretail.paymentservice.entity.Payment;
import java.util.UUID;
import org.springframework.beans.BeanUtils;

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
