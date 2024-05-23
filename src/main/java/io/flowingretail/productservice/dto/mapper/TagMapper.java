package io.flowingretail.productservice.dto.mapper;

import io.flowingretail.productservice.dto.TagDto;
import io.flowingretail.productservice.adapter.out.jpa.Tag;
import org.springframework.beans.BeanUtils;

public class TagMapper {
    public static TagDto toDto(Tag entity) {
        var dto = new TagDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static Tag toEntity(TagDto dto) {
        var entity = new Tag();
        BeanUtils.copyProperties(dto,entity);
        return entity;
    }
}
