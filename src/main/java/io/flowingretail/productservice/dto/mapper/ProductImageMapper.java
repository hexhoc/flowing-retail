package io.flowingretail.productservice.dto.mapper;

import io.flowingretail.productservice.dto.ProductImageDTO;
import io.flowingretail.productservice.entity.ProductImage;
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
