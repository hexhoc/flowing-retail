package io.flowing.retail.order.process.payload;

public class OrderCompletedEventPayload {
  
  private String orderId;

  public String getOrderId() {
    return orderId;
  }

  public OrderCompletedEventPayload setOrderId(String orderId) {
    this.orderId = orderId;
    return this;
  }
}