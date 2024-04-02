package io.flowingretail.productservice.dto.mapper;

import io.flowingretail.productservice.dto.TagDTO;
import io.flowingretail.productservice.entity.Tag;
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
