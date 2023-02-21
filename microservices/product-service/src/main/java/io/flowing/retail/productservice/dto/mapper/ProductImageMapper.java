package io.flowing.retail.productservice.dto.mapper;

import io.flowing.retail.productservice.dto.ProductImageDTO;
import io.flowing.retail.productservice.entity.ProductImage;
import org.springframework.beans.BeanUtils;

public class ProductImageMapper {
    public static ProductImageDTO toDto(ProductImage entity) {
        var dto = new ProductImageDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static ProductImage toEntity(ProductImageDTO dto) {
        var entity = new ProductImage();
        BeanUtils.copyProperties(dto,entity);
        return entity;
    }
}
