package io.flowingretail.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Order information")
public class InventoryItemDTO {
    @Schema(description = "product id")
    private Integer productId;

    @Schema(description = "quantity")
    @NotBlank
    @Min(1)
    @Max(999999999)
    private Integer quantity;
}
