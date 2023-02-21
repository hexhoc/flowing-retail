package io.flowing.retail.inventoryservice.controller.payload;

import io.flowing.retail.inventoryservice.dto.ProductStockDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductStockBatchRequest {
    // TODO: Set size limit
    private List<ProductStockDTO> stocks;
}
