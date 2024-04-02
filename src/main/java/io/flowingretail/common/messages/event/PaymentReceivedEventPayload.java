package io.flowingretail.common.messages.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentReceivedEventPayload {
  private String refId;
  private String paymentId;
}
