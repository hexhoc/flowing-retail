package io.flowingretail.inventoryservice.adapter.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}
