package io.flowing.retail.inventory.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.inventory.application.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MessageListener {
  
  private final MessageSender messageSender;
  
  private final InventoryService inventoryService;

  private final ObjectMapper objectMapper;
  
  @Transactional
  @KafkaListener(id = "inventory", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{
    if ("FetchGoodsCommand".equals(messageType)) {
      Message<FetchGoodsCommandPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<Message<FetchGoodsCommandPayload>>() {});

      FetchGoodsCommandPayload fetchGoodsCommand = message.getData();
      String pickId = inventoryService.pickItems( //
              fetchGoodsCommand.getItems(), fetchGoodsCommand.getReason(), fetchGoodsCommand.getRefId());

      messageSender.send( //
              new Message<GoodsFetchedEventPayload>( //
                      "GoodsFetchedEvent", //
                      message.getTraceid(), //
                      new GoodsFetchedEventPayload() //
                              .setRefId(fetchGoodsCommand.getRefId())
                              .setPickId(pickId))
                      .setCorrelationid(message.getCorrelationid()));
    }
  }
}
