package io.flowing.retail.order.messages.command;

public class RetrievePaymentCommandPayload {
  
  private String refId;
  private Integer customerId;

  private String reason;
  private double amount;
  
  public String getRefId() {
    return refId;
  }
  public RetrievePaymentCommandPayload setRefId(String refId) {
    this.refId = refId;
    return this;
  }

  public Integer getCustomerId() {
    return customerId;
  }

  public RetrievePaymentCommandPayload setCustomerId(Integer customerId) {
    this.customerId = customerId;
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
