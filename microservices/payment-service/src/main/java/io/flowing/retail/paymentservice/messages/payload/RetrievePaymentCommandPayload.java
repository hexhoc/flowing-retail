package io.flowing.retail.paymentservice.messages.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrievePaymentCommandPayload {
  // Order id
  private String refId;
  private Integer customerId;
  private String reason;
  private double amount;

}
