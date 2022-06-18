package io.flowing.retail.order.dto;

import lombok.Data;

@Data
public class OrderItemDto {
  private String articleId;
  private int amount;
}
