package io.flowing.retail.inventoryservice.repository;

import io.flowing.retail.inventoryservice.entity.ProductStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductStockRepository extends JpaRepository<ProductStock, ProductStock.ProductStockId> {
    @Query(value = """
    select s
    from ProductStock s
    where s.productStockId.warehouseId = :warehouseId
    """)
    Page<ProductStock> findAllByWarehouseId(Integer warehouseId, Pageable pageable);

    @Query(value = """
    select s
    from ProductStock s
    where s.productStockId.productId = :productId
    """)
    Page<ProductStock> findAllByProductId(Integer productId, Pageable pageable);

    @Query(value = """
    select s
    from ProductStock s
    where s.productStockId.productId = :productId and s.productStockId.warehouseId = :warehouseId
    """)
    Page<ProductStock> findAllByProductIdAndWarehouseId(Integer productId, Integer warehouseId, Pageable pageable);

    @Query(value = """
    select s
    from ProductStock s
    where s.productStockId.productId in (:productIds) and s.productStockId.warehouseId = :warehouseId
    """)
    List<ProductStock> findAllForPickUp(List<Integer> productIds, Integer warehouseId);

}