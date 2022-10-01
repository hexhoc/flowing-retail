package io.flowing.retail.inventory.domain;

import lombok.Data;

@Data
public class Item {

  private String articleId;
  private int amount;

}
