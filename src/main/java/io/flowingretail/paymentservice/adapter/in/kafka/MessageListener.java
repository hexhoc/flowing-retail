package io.flowingretail.paymentservice.adapter.in.kafka;

import static io.flowingretail.common.constants.EventTypeConstants.RETRIEVE_PAYMENT_COMMAND;

import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.paymentservice.adapter.in.kafka.command.RetrievePaymentCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

@RequiredArgsConstructor
@Slf4j
public class MessageListener {
  private final ApplicationEventPublisher applicationEventPublisher;

  @KafkaListener(id = "payment", topics = KafkaConfig.PAYMENT_TOPIC)
  public void paymentEventReceived(String messagePayloadJson,
                                   @Header("type") String messageType) {
    if (!RETRIEVE_PAYMENT_COMMAND.equals(messageType)) {
      log.info("Ignoring message of type " + messageType);
      return;
    }

    applicationEventPublisher.publishEvent(new RetrievePaymentCommand(this, messagePayloadJson));
  }
}