package io.flowingretail.productservice.dto.mapper;

import io.flowingretail.productservice.dto.ReviewDto;
import io.flowingretail.productservice.adapter.out.jpa.Review;
import org.springframework.beans.BeanUtils;

public class ReviewMapper {
    public static ReviewDto toDto(Review entity) {
        var dto = new ReviewDto();
        BeanUtils.copyProperties(entity, dto);
        // TODO: Check that is work if product has lezy fetchtype
        dto.setProductId(entity.getProduct().getId());
        return dto;
    }

    public static Review toEntity(ReviewDto dto) {
        var entity = new Review();
        BeanUtils.copyProperties(dto,entity, "version","createdDate","modifiedDate");
        return entity;
    }
}
