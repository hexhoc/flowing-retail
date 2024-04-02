package io.flowingretail.inventoryservice.repository;

import io.flowingretail.inventoryservice.entity.ProductStock;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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