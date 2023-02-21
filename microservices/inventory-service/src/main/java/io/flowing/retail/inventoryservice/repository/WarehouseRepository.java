package io.flowing.retail.inventoryservice.repository;

import io.flowing.retail.inventoryservice.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}
