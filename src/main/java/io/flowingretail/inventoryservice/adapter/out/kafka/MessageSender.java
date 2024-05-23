package io.flowingretail.inventoryservice.adapter.out.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.adapter.in.kafka.Message;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

/**
 * Helper to send messages, currently nailed to Kafka, but could also send via AMQP (e.g. RabbitMQ) or
 * any other transport easily
 */
@Component("messageSenderInventory")
@RequiredArgsConstructor
@Slf4j
public class MessageSender {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
    
  public void send(Message<?> m, String topicName) {
    try {
      // avoid too much magic and transform ourselves
      String jsonMessage = objectMapper.writeValueAsString(m);
      
      // wrap into a proper message for Kafka including a header
      ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, jsonMessage);
      record.headers().add("type", m.getType().getBytes());

      // and send it
      CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
      future.whenComplete(
          (res, error) -> {
            if (error != null) {
              log.error("Unable to deliver message [{}]. {}", res, error.getMessage());
            } else if (res != null) {
              log.info("Message [{}] delivered with offset {}", res, res.getRecordMetadata().offset());
            }
          });
    } catch (Exception e) {
      throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
    }
  }
}
