package io.flowing.retail.productservice.repository;

import io.flowing.retail.productservice.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer>, JpaSpecificationExecutor<ProductImage> {

    List<ProductImage> findAllByProductId(Integer productId);

    @Modifying
    void deleteByNameAndProductId(String name, Integer productId);
}