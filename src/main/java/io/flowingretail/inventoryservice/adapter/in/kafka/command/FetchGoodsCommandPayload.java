package io.flowingretail.inventoryservice.adapter.in.kafka.command;

import io.flowingretail.inventoryservice.dto.InventoryItemDto;
import java.util.LinkedHashSet;
import java.util.Set;

public class FetchGoodsCommandPayload {
  
  private String refId;
  private String reason = "CustomerOrder";
  private Set<InventoryItemDto> items = new LinkedHashSet<>();
  
  public String getRefId() {
    return refId;
  }
  public FetchGoodsCommandPayload setRefId(String refId) {
    this.refId = refId;
    return this;
  }

  public String getReason() {
    return reason;
  }
  public FetchGoodsCommandPayload setReason(String reason) {
    this.reason = reason;
    return this;
  }
  public Set<InventoryItemDto> getItems() {
    return items;
  }
  public FetchGoodsCommandPayload setItems(Set<InventoryItemDto> items) {
    this.items = items;
    return this;
  }

}
