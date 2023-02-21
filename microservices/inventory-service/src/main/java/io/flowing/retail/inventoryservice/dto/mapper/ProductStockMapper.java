package io.flowing.retail.inventoryservice.dto.mapper;

import io.flowing.retail.inventoryservice.dto.ProductStockDTO;
import io.flowing.retail.inventoryservice.entity.ProductStock;

public class ProductStockMapper {
    public static ProductStockDTO toDto(ProductStock entity) {
        var dto = new ProductStockDTO();
        dto.setProductId(entity.getProductStockId().getProductId());
        dto.setWarehouseId(entity.getProductStockId().getWarehouseId());
        dto.setStock(entity.getStock());
        return dto;
    }

    public static ProductStock toEntity(ProductStockDTO dto) {
        var entity = new ProductStock();
        var entityId = new ProductStock.ProductStockId();
        entityId.setProductId(dto.getProductId());
        entityId.setWarehouseId(dto.getWarehouseId());
        entity.setProductStockId(entityId);
        entity.setStock(dto.getStock());
        return entity;
    }
}
