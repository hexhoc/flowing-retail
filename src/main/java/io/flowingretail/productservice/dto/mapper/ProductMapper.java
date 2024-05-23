package io.flowingretail.productservice.dto.mapper;

import io.flowingretail.productservice.dto.ProductDto;
import io.flowingretail.productservice.adapter.out.jpa.Product;
import io.flowingretail.productservice.utils.Memoizer;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

public class ProductMapper {

    public static Memoizer<Integer, String> memoizer;
    static {
        memoizer = new Memoizer<>(
                arg -> {
                    // Calculate SKU
                    String prefix = "22000";
                    String postfix = "1233";
                    return prefix + arg + postfix;
                }
        );
    }

    public static ProductDto toDto(Product entity) {
        var dto = new ProductDto();
        BeanUtils.copyProperties(entity, dto);
        dto.setCategory(CategoryMapper.toDto(entity.getCategory()));
        dto.setImages(entity.getProductImages().stream().map(ProductImageMapper::toDto).collect(Collectors.toSet()));
        dto.setTags(entity.getTags().stream().map(TagMapper::toDto).collect(Collectors.toSet()));
        try {
            dto.setSku(memoizer.compute(dto.getId()));
        } catch (InterruptedException e) {
            dto.setSku("");
        }
        return dto;
    }

    public static Product toEntity(ProductDto dto) {
        var entity = new Product();
        BeanUtils.copyProperties(dto,entity,"version","createdDate","modifiedDate");
        entity.setCategory(CategoryMapper.toEntity(dto.getCategory()));
        // Images are uploaded via a separate controller
        //  entity.setImages(dto.getImages().stream().map(ImageMapper::toEntity).collect(Collectors.toSet()));
        entity.setTags(dto.getTags().stream().map(TagMapper::toEntity).collect(Collectors.toSet()));

        return entity;
    }
}
