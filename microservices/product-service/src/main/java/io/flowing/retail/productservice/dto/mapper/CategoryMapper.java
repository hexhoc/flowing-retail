package io.flowing.retail.productservice.dto.mapper;

import io.flowing.retail.productservice.dto.CategoryDTO;
import io.flowing.retail.productservice.entity.Category;
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
