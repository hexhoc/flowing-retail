package io.flowingretail.inventoryservice.dto;

import io.flowingretail.common.dto.InventoryItemDto;
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
