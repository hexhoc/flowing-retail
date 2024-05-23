package io.flowingretail.inventoryservice.dto.mapper;

import io.flowingretail.inventoryservice.dto.ProductStockDto;
import io.flowingretail.inventoryservice.adapter.out.jpa.ProductStock;

public class ProductStockMapper {
    public static ProductStockDto toDto(ProductStock entity) {
        var dto = new ProductStockDto();
        dto.setProductId(entity.getProductStockId().getProductId());
        dto.setWarehouseId(entity.getProductStockId().getWarehouseId());
        dto.setStock(entity.getStock());
        return dto;
    }

    public static ProductStock toEntity(ProductStockDto dto) {
        var entity = new ProductStock();
        var entityId = new ProductStock.ProductStockId();
        entityId.setProductId(dto.getProductId());
        entityId.setWarehouseId(dto.getWarehouseId());
        entity.setProductStockId(entityId);
        entity.setStock(dto.getStock());
        return entity;
    }
}
