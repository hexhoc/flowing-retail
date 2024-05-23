package io.flowingretail.shippingservice.adapter.in.kafka;

import static io.flowingretail.common.constants.EventTypeConstants.SHIP_GOODS_COMMAND;

import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.shippingservice.adapter.in.kafka.command.ShipGoodsCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

@RequiredArgsConstructor
@Slf4j
public class MessageListener {
  private final ApplicationEventPublisher applicationEventPublisher;

  @KafkaListener(id = "shipping", topics = KafkaConfig.SHIPMENT_TOPIC)
  public void messageReceived(String messagePayloadJson,
                              @Header("type") String messageType) {
    if (!SHIP_GOODS_COMMAND.equals(messageType)) {
      log.info("Ignoring message of type " + messageType);
      return;
    }

    applicationEventPublisher.publishEvent(new ShipGoodsCommand(this, messagePayloadJson));
  }
}