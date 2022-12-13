package io.flowing.retail.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "Order's items information")
public class OrderItemDto {
  @Schema(description = "Article id (Product article)", example = "AR-19293", required = true)
  @NotBlank
  private String articleId;

  @Schema(description = "Total amount", required = true)
  @NotBlank
  @Min(1)
  @Max(999999999)
  private int amount;
}
