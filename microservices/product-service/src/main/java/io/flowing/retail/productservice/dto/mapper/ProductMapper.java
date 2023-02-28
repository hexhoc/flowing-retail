package io.flowing.retail.productservice.dto.mapper;

import io.flowing.retail.productservice.dto.ProductDTO;
import io.flowing.retail.productservice.entity.Product;
import io.flowing.retail.productservice.utils.Memoizer;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

public class ProductMapper {

    public static Memoizer<Integer, String> memoizer;
    static {
        memoizer = new Memoizer<>(
                arg -> {
                    // Calculate SKU
                    String prefix = "22000";
                    String postfix = "1233";
                    Thread.sleep(2_500);
                    return prefix + arg + postfix;
                }
        );
    }

    public static ProductDTO toDto(Product entity) {
        var dto = new ProductDTO();
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

    public static Product toEntity(ProductDTO dto) {
        var entity = new Product();
        BeanUtils.copyProperties(dto,entity,"version","createdDate","modifiedDate");
        entity.setCategory(CategoryMapper.toEntity(dto.getCategory()));
        // Images are uploaded via a separate controller
        //  entity.setImages(dto.getImages().stream().map(ImageMapper::toEntity).collect(Collectors.toSet()));
        entity.setTags(dto.getTags().stream().map(TagMapper::toEntity).collect(Collectors.toSet()));

        return entity;
    }
}
