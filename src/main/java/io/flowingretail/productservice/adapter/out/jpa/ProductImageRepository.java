package io.flowingretail.productservice.adapter.out.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer>, JpaSpecificationExecutor<ProductImage> {

    List<ProductImage> findAllByProductId(Integer productId);

    @Modifying
    void deleteByNameAndProductId(String name, Integer productId);
}