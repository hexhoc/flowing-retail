package io.flowing.retail.order.process.payload;

import java.util.ArrayList;
import java.util.List;

import io.flowing.retail.order.dto.OrderItemDto;

public class FetchGoodsCommandPayload {
  
  private String refId;
  private String reason = "CustomerOrder";
  private List<OrderItemDto> items = new ArrayList<>();
  
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
  public List<OrderItemDto> getItems() {
    return items;
  }
  public FetchGoodsCommandPayload setItems(List<OrderItemDto> items) {
    this.items = items;
    return this;
  }

}
