package io.flowingretail.productservice.dto.mapper;

import io.flowingretail.productservice.dto.CategoryDto;
import io.flowingretail.productservice.entity.Category;
import org.springframework.beans.BeanUtils;

public class CategoryMapper {
    public static CategoryDto toDto(Category entity) {
        var dto = new CategoryDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Category toEntity(CategoryDto dto) {
        var entity = new Category();
        BeanUtils.copyProperties(dto,entity, "version","createdDate","modifiedDate");
        return entity;
    }
}
