package io.flowingretail.inventoryservice.adapter.in.kafka;

import static io.flowingretail.common.constants.EventTypeConstants.FETCH_GOODS_COMMAND;

import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.inventoryservice.adapter.in.kafka.command.FetchGoodsCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

@RequiredArgsConstructor
@Slf4j
public class MessageListener {
  private final ApplicationEventPublisher applicationEventPublisher;

  @KafkaListener(id = "inventory", topics = KafkaConfig.INVENTORY_TOPIC)
  public void inventoryEventReceived(String messagePayloadJson,
                                     @Header("type") String messageType) {
    if (!FETCH_GOODS_COMMAND.equals(messageType)) {
      log.info("Ignoring message of type " + messageType);
      return;
    }

    applicationEventPublisher.publishEvent(new FetchGoodsCommand(this, messagePayloadJson));
  }
}