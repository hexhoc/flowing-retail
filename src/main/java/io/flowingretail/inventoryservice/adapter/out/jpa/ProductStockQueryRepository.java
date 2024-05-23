package io.flowingretail.inventoryservice.adapter.out.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/*
Repository for read. In this example, reading from the same database that is being written to
 */
public interface ProductStockQueryRepository extends JpaRepository<ProductStock, ProductStock.ProductStockId> {
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


}