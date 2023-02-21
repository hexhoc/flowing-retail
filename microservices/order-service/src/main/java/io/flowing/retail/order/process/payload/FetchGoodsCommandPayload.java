package io.flowing.retail.order.process.payload;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.flowing.retail.order.dto.OrderItemDTO;

public class FetchGoodsCommandPayload {
  
  private String refId;
  private String reason = "CustomerOrder";
  private Set<OrderItemDTO> items = new LinkedHashSet<>();
  
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
  public Set<OrderItemDTO> getItems() {
    return items;
  }
  public FetchGoodsCommandPayload setItems(Set<OrderItemDTO> items) {
    this.items = items;
    return this;
  }

}
