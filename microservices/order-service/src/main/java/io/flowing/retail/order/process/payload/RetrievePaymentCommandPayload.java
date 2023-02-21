package io.flowing.retail.order.process.payload;

public class RetrievePaymentCommandPayload {
  
  private String refId;
  private String reason;
  private double amount;
  
  public String getRefId() {
    return refId;
  }
  public RetrievePaymentCommandPayload setRefId(String refId) {
    this.refId = refId;
    return this;
  }
  public String getReason() {
    return reason;
  }
  public RetrievePaymentCommandPayload setReason(String reason) {
    this.reason = reason;
    return this;
  }
  public double getAmount() {
    return amount;
  }
  public RetrievePaymentCommandPayload setAmount(double amount) {
    this.amount = amount;
    return this;
  }
}
