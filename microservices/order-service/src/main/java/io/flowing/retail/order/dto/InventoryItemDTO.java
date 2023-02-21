package io.flowing.retail.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
