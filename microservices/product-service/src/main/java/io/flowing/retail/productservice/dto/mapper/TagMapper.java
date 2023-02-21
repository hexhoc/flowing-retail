package io.flowing.retail.productservice.dto.mapper;

import io.flowing.retail.productservice.dto.TagDTO;
import io.flowing.retail.productservice.entity.Tag;
import org.springframework.beans.BeanUtils;

public class TagMapper {
    public static TagDTO toDto(Tag entity) {
        var dto = new TagDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Tag toEntity(TagDTO dto) {
        var entity = new Tag();
        BeanUtils.copyProperties(dto,entity);
        return entity;
    }
}
