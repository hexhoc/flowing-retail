package io.flowingretail.common.messages;

import static io.flowingretail.common.constants.EventTypeConstants.FETCH_GOODS_COMMAND;
import static io.flowingretail.common.constants.EventTypeConstants.GOODS_FETCHED_EVENT;
import static io.flowingretail.common.constants.EventTypeConstants.GOODS_SHIPPED_EVENT;
import static io.flowingretail.common.constants.EventTypeConstants.PAYMENT_RECEIVED_EVENT;
import static io.flowingretail.common.constants.EventTypeConstants.RETRIEVE_PAYMENT_COMMAND;
import static io.flowingretail.common.constants.EventTypeConstants.SHIP_GOODS_COMMAND;

import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.messages.command.FetchGoodsCommand;
import io.flowingretail.common.messages.command.RetrievePaymentCommand;
import io.flowingretail.common.messages.command.ShipGoodsCommand;
import io.flowingretail.common.messages.event.GoodsFetchedEvent;
import io.flowingretail.common.messages.event.GoodsShippedEvent;
import io.flowingretail.common.messages.event.PaymentReceivedEvent;
import io.flowingretail.common.service.IncomingEventService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageListener {
  private final ApplicationEventPublisher applicationEventPublisher;

  @KafkaListener(id = "order", topics = {KafkaConfig.ORDER_TOPIC})
  public void orderEventListener(String messagePayloadJson,
                                 @Header("type") String eventType) {
    log.info(eventType);

    if (PAYMENT_RECEIVED_EVENT.equals(eventType)) {
      applicationEventPublisher.publishEvent(new PaymentReceivedEvent(this, messagePayloadJson));
    } else if (GOODS_FETCHED_EVENT.equals(eventType)) {
      applicationEventPublisher.publishEvent(new GoodsFetchedEvent(this, messagePayloadJson));
    } else if (GOODS_SHIPPED_EVENT.equals(eventType)) {
      applicationEventPublisher.publishEvent(new GoodsShippedEvent(this, messagePayloadJson));
    } else {
      log.info("Ignored message of type " + eventType);
    }
  }

  @KafkaListener(id = "payment", topics = KafkaConfig.PAYMENT_TOPIC)
  public void paymentEventReceived(String messagePayloadJson,
                                   @Header("type") String messageType) {
    if (!RETRIEVE_PAYMENT_COMMAND.equals(messageType)) {
      log.info("Ignoring message of type " + messageType);
      return;
    }

    applicationEventPublisher.publishEvent(new RetrievePaymentCommand(this, messagePayloadJson));
  }

  @KafkaListener(id = "inventory", topics = KafkaConfig.INVENTORY_TOPIC)
  public void inventoryEventReceived(String messagePayloadJson,
                                     @Header("type") String messageType) {
    if (!FETCH_GOODS_COMMAND.equals(messageType)) {
      log.info("Ignoring message of type " + messageType);
      return;
    }

    applicationEventPublisher.publishEvent(new FetchGoodsCommand(this, messagePayloadJson));
  }

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