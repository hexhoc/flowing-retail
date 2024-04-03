package io.flowingretail.inventoryservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
@Schema(description = "Product stock data transfer object. Used for request and response")
public class ProductStockDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Product id")
    @NotNull
    private Integer productId;

    @Schema(description = "Warehouse id")
    @NotNull
    private Integer warehouseId;

    @Schema(description = "Total stock")
    @NotNull
    private Integer stock;
}
