package io.flowingretail.productservice.dto.mapper;

import io.flowingretail.productservice.dto.ProductImageDto;
import io.flowingretail.productservice.entity.ProductImage;
import org.springframework.beans.BeanUtils;

public class ProductImageMapper {
    public static ProductImageDto toDto(ProductImage entity) {
        var dto = new ProductImageDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static ProductImage toEntity(ProductImageDto dto) {
        var entity = new ProductImage();
        BeanUtils.copyProperties(dto,entity);
        return entity;
    }
}
