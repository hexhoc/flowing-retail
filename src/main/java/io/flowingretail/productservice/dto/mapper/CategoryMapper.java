package io.flowingretail.productservice.dto.mapper;

import io.flowingretail.productservice.dto.CategoryDTO;
import io.flowingretail.productservice.entity.Category;
import org.springframework.beans.BeanUtils;

public class CategoryMapper {
    public static CategoryDTO toDto(Category entity) {
        var dto = new CategoryDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Category toEntity(CategoryDTO dto) {
        var entity = new Category();
        BeanUtils.copyProperties(dto,entity, "version","createdDate","modifiedDate");
        return entity;
    }
}
