package io.flowing.retail.monitor.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.monitor.domain.PastEvent;
import io.flowing.retail.monitor.persistence.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageListener {

  private final SimpMessagingTemplate simpMessageTemplate;
  
  private final ObjectMapper objectMapper;

  @Transactional
  @KafkaListener(id = "monitor", topics = {"payment","inventory","shipment"})
  public void messageReceived(String messageJson) throws Exception {
    log.info(messageJson);
    Message<JsonNode> message = objectMapper.readValue( //
        messageJson, //
        new TypeReference<Message<JsonNode>>() {});
    
    String type = "Event";
    if (message.getType().endsWith("Command")) {
      type = "Command";
    }
    
    PastEvent event = new PastEvent( //
        type, //
        message.getType(), //
        message.getTraceid(), //
        message.getSource(), //
        message.getData().toString());
    event.setSourceJson(messageJson);
    
    // save
    LogRepository.instance.addEvent(event);
    
    // and probably send to connected websocket (TODO: Not a good place for the code here!)
    simpMessageTemplate.convertAndSend("/topic/events", event);
  }
}
