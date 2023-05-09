package io.flowing.retail.order.messages.command;

import java.util.LinkedHashSet;
import java.util.Set;
import io.flowing.retail.order.dto.InventoryItemDTO;

public class FetchGoodsCommandPayload {
  
  private String refId;
  private String reason = "CustomerOrder";
  private Set<InventoryItemDTO> items = new LinkedHashSet<>();
  
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
  public Set<InventoryItemDTO> getItems() {
    return items;
  }
  public FetchGoodsCommandPayload setItems(Set<InventoryItemDTO> items) {
    this.items = items;
    return this;
  }

}
