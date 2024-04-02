package io.flowingretail.inventoryservice.dto;

import io.flowingretail.common.dto.InventoryItemDTO;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class PickOrderDTO {

  private String id = UUID.randomUUID().toString();
  private Set<InventoryItemDTO> items;

  public PickOrderDTO(Set<InventoryItemDTO> items) {
    this.items = items;
  }
}
