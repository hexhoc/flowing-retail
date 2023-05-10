package io.flowing.retail.inventoryservice.controller.payload;

import javax.validation.constraints.Size;

import io.flowing.retail.inventoryservice.dto.ProductStockDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductStockBatchRequest {
    @Size(min=1, max=100, message = "лимит пакета 100 записей на обновление")
    private List<ProductStockDTO> stocks;
}
