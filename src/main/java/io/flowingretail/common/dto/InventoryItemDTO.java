package io.flowingretail.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryItemDTO {
    private Integer productId;
    private Integer quantity;
}
