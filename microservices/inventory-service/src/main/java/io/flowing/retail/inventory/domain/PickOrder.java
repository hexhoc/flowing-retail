package io.flowing.retail.inventory.domain;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PickOrder {

  private String pickId = UUID.randomUUID().toString(); 
  private List<Item> items;

  public PickOrder(List<Item> items) {
    this.items = items;
  }
}
