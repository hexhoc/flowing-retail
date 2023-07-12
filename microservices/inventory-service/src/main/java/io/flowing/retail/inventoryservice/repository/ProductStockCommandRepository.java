package io.flowing.retail.inventoryservice.repository;

import io.flowing.retail.inventoryservice.entity.ProductStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
Repository for write. In this example, writing to the same database that is being reading
 */
public interface ProductStockCommandRepository extends JpaRepository<ProductStock, ProductStock.ProductStockId> {
    @Query(value = """
    select s
    from ProductStock s
    where s.productStockId.productId in (:productIds) and s.productStockId.warehouseId = :warehouseId
    """)
    List<ProductStock> findAllForPickUp(List<Integer> productIds, Integer warehouseId);

}