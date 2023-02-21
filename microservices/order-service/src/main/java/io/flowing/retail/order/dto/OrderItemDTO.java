package io.flowing.retail.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

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
