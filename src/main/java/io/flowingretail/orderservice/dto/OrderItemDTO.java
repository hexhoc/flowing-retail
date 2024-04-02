package io.flowingretail.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Schema(description = "Order's items information")
public class OrderItemDTO implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "id")
  private String id;

  @Schema(description = "product id")
  private Integer productId;

  @Schema(description = "quantity")
  @NotBlank
  @Min(1)
  @Max(999999999)
  private Integer quantity;

  @Schema(description = "price")
  @NotBlank
  @Min(1)
  @Max(999999999)
  private BigDecimal price;
}
