package io.flowingretail.inventoryservice.repository;

import io.flowingretail.inventoryservice.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}
