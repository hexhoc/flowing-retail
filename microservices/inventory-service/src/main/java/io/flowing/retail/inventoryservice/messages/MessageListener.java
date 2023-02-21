package io.flowing.retail.inventoryservice.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.inventoryservice.messages.payload.FetchGoodsCommandPayload;
import io.flowing.retail.inventoryservice.messages.payload.GoodsFetchedEventPayload;
import io.flowing.retail.inventoryservice.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MessageListener {

  private final MessageSender messageSender;
  private final ProductStockService productStockService;
  private final ObjectMapper objectMapper;
  
  @Transactional
  @KafkaListener(id = "inventory", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{
    if ("FetchGoodsCommand".equals(messageType)) {
      Message<FetchGoodsCommandPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<>() { });

      FetchGoodsCommandPayload fetchGoodsCommand = message.getData();
      productStockService.pickItems( //
              fetchGoodsCommand.getItems(), fetchGoodsCommand.getReason(), fetchGoodsCommand.getRefId());

      messageSender.send( //
              new Message<>( //
                      "GoodsFetchedEvent", //
                      message.getTraceid(), //
                      new GoodsFetchedEventPayload(fetchGoodsCommand.getRefId(), fetchGoodsCommand.getRefId()))
                      .setCorrelationid(message.getCorrelationid()));
    }
  }
    
    
}
