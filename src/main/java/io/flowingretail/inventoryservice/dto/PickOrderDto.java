package io.flowingretail.inventoryservice.dto;

import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class PickOrderDto {

  private String id = UUID.randomUUID().toString();
  private Set<InventoryItemDto> items;

  public PickOrderDto(Set<InventoryItemDto> items) {
    this.items = items;
  }
}
