package io.flowingretail.paymentservice.adapter.in.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentReceivedEventPayload {
  private String refId;
  private String paymentId;
}
