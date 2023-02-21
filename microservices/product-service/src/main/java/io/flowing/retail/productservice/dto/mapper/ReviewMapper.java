package io.flowing.retail.productservice.dto.mapper;

import io.flowing.retail.productservice.dto.ReviewDTO;
import io.flowing.retail.productservice.entity.Review;
import org.springframework.beans.BeanUtils;

public class ReviewMapper {
    public static ReviewDTO toDto(Review entity) {
        var dto = new ReviewDTO();
        BeanUtils.copyProperties(entity, dto);
        // TODO: Check that is work if product has lezy fetchtype
        dto.setProductId(entity.getProduct().getId());
        return dto;
    }

    public static Review toEntity(ReviewDTO dto) {
        var entity = new Review();
        BeanUtils.copyProperties(dto,entity, "version","createdDate","modifiedDate");
        return entity;
    }
}
