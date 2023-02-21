package io.flowing.retail.inventoryservice.domain;

import io.flowing.retail.inventoryservice.dto.InventoryItemDTO;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class PickOrder {

  private String id = UUID.randomUUID().toString();
  private Set<InventoryItemDTO> items;

  public PickOrder(Set<InventoryItemDTO> items) {
    this.items = items;
  }
}
