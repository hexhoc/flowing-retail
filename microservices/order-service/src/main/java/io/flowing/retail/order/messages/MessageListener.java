package io.flowing.retail.order.messages;

import java.util.Collections;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import io.camunda.zeebe.client.ZeebeClient;
import io.flowing.retail.order.process.payload.GoodsFetchedEventPayload;
import io.flowing.retail.order.process.payload.GoodsShippedEventPayload;
import io.flowing.retail.order.process.payload.PaymentReceivedEventPayload;
import io.flowing.retail.order.service.OrderService;

@Component
@RequiredArgsConstructor
@Log
public class MessageListener {
  private final ZeebeClient zeebe;
  private final ObjectMapper objectMapper;
  private final OrderService orderService;

  @KafkaListener(id = "order", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception {

    if ("PaymentReceivedEvent".equals(messageType)) {
      paymentReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {
      }));
    } else if ("GoodsFetchedEvent".equals(messageType)) {
      goodsFetchedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {
      }));
    } else if ("GoodsShippedEvent".equals(messageType)) {
      goodsShippedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {
      }));
    } else {
      log.info("Ignored message of type " + messageType);
    }
  }

  public void paymentReceived(Message<PaymentReceivedEventPayload> message) {
    log.info("paymentReceived");

    PaymentReceivedEventPayload paymentReceivedEventPayload = message.getData();
    orderService.updateStatus(paymentReceivedEventPayload.getRefId(), OrderStatusEnum.PAID);

    zeebe.newPublishMessageCommand() //
        .messageName(message.getType())
        .correlationKey(message.getCorrelationid())
        .variables(Collections.singletonMap("paymentInfo", "YeahWeCouldAddSomething"))
        .send().join();

    log.info("Correlated " + message);
  }

  public void goodsFetchedReceived(Message<GoodsFetchedEventPayload> message) {
    log.info("goodsFetchedReceived");

    GoodsFetchedEventPayload goodsFetchedEventPayload = message.getData();
    orderService.updateStatus(goodsFetchedEventPayload.getRefId(), OrderStatusEnum.ALLOCATED);

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
    orderService.updateStatus(goodsShippedEventPayload.getRefId(), OrderStatusEnum.DELIVERED);

    zeebe.newPublishMessageCommand() //
        .messageName(message.getType()) //
        .correlationKey(message.getCorrelationid()) //
        .variables(Collections.singletonMap("shipmentId", goodsShippedEventPayload.getShipmentId())) //
        .send().join();

    log.info("Correlated " + message);
  }
}
