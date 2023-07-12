package io.flowing.retail.inventoryservice.dto;

import io.flowing.retail.inventoryservice.dto.InventoryItemDTO;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class PickOrderDTO {

  private String id = UUID.randomUUID().toString();
  private Set<InventoryItemDTO> items;

  public PickOrderDTO(Set<InventoryItemDTO> items) {
    this.items = items;
  }
}
