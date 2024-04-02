package io.flowingretail.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class ProductStockQuery {
    private Page<ProductStockDTO> stocks;
}
