package io.flowing.retail.order.messages;

import java.util.Collections;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.order.config.KafkaConfig;
import io.flowing.retail.order.entity.enums.OrderStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import io.camunda.zeebe.client.ZeebeClient;
import io.flowing.retail.order.process.payload.event.GoodsFetchedEventPayload;
import io.flowing.retail.order.process.payload.event.GoodsShippedEventPayload;
import io.flowing.retail.order.process.payload.event.PaymentReceivedEventPayload;
import io.flowing.retail.order.service.OrderService;

@Component
@RequiredArgsConstructor
@Log
public class MessageListener {
  private final ZeebeClient zeebe;
  private final ObjectMapper objectMapper;
  private final OrderService orderService;

  @KafkaListener(id = "order", topics = KafkaConfig.PAYMENT_TOPIC)
  public void paymentListener(String messagePayloadJson, @Header("type") String messageType) throws Exception {
    log.info(messageType);
    paymentReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {}));
  }

  @KafkaListener(id = "order", topics = KafkaConfig.INVENTORY_TOPIC)
  public void inventoryListener(String messagePayloadJson, @Header("type") String messageType) throws Exception {
    log.info(messageType);
    goodsFetchedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {}));
  }
  @KafkaListener(id = "order", topics = KafkaConfig.SHIPMENT_TOPIC)
  public void shipmentListener(String messagePayloadJson, @Header("type") String messageType) throws Exception {
    log.info(messageType);
    goodsShippedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {}));
  }

  public void paymentReceived(Message<PaymentReceivedEventPayload> message) {
    log.info("paymentReceived");

    PaymentReceivedEventPayload paymentReceivedEventPayload = message.getData();
    orderService.updateStatus(paymentReceivedEventPayload.getRefId(), OrderStatusEnum.PICK_UP);
    // TODO: set in variables. Payment success or not
    zeebe.newPublishMessageCommand() //
        .messageName(message.getType())
        .correlationKey(message.getCorrelationid())
        .variables(Collections.singletonMap("paymentId", paymentReceivedEventPayload.getPaymentId()))
        .send().join();

    log.info("Correlated " + message);
  }

  public void goodsFetchedReceived(Message<GoodsFetchedEventPayload> message) {
    log.info("goodsFetchedReceived");

    GoodsFetchedEventPayload goodsFetchedEventPayload = message.getData();
    orderService.updateStatus(goodsFetchedEventPayload.getRefId(), OrderStatusEnum.SHIPMENT_READY);
    // TODO: set in variables. Fetched success or not
    zeebe.newPublishMessageCommand() //
        .messageName(message.getType()) //
        .correlationKey(message.getCorrelationid()) //
        .variables(Collections.singletonMap("pickId", goodsFetchedEventPayload.getPickId())) //
        .send().join();

    log.info("Correlated " + message);
  }

  public void goodsShippedReceived(Message<GoodsShippedEventPayload> message) {
    log.info("goodsShippedReceived");

    GoodsShippedEventPayload goodsShippedEventPayload = message.getData();
    orderService.updateStatus(goodsShippedEventPayload.getRefId(), OrderStatusEnum.DONE);
    // TODO: set in variables. Shipment success or not
    zeebe.newPublishMessageCommand() //
        .messageName(message.getType()) //
        .correlationKey(message.getCorrelationid()) //
        .variables(Collections.singletonMap("shipmentId", goodsShippedEventPayload.getShipmentId())) //
        .send().join();

    log.info("Correlated " + message);
  }
}
