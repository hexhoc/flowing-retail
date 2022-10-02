package io.flowing.retail.order.dto;

import lombok.Data;

@Data
public class OrderItemDto {
  //TODO add springdoc

  private String articleId;
  private int amount;
}
