package io.flowing.retail.inventoryservice.messages.payload;

import java.util.LinkedHashSet;
import java.util.Set;

import io.flowing.retail.inventoryservice.dto.InventoryItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FetchGoodsCommandPayload {
  
  private String refId;
  private String reason = "CustomerOrder";
  private Set<InventoryItemDTO> items = new LinkedHashSet<>();

}
