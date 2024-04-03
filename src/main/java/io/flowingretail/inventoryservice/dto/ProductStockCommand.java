package io.flowingretail.inventoryservice.dto;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class ProductStockCommand {
    @Size(min=1, max=100, message = "лимит пакета 100 записей на обновление")
    private List<ProductStockDto> stocks;
}
