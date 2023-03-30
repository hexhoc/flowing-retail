package io.flowing.retail.shippingservice.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Helper to send messages, currently nailed to Kafka, but could also send via AMQP (e.g. RabbitMQ) or
 * any other transport easily
 */
@Component
@RequiredArgsConstructor
public class MessageSender {

  public static final String TOPIC_NAME = "shipment";
  
  private final KafkaTemplate<String, String> kafkaTemplate;
  
  private final ObjectMapper objectMapper;
  
  @Bean
  public NewTopic autoCreateTopicOnStartupIfNotExistant() {
    return TopicBuilder.name(TOPIC_NAME).partitions(1).replicas(1).build();
  }
    
  public void send(Message<?> m) {
    try {
      // avoid too much magic and transform ourselves
      String jsonMessage = objectMapper.writeValueAsString(m);
      
      // wrap into a proper message for Kafka including a header
      ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC_NAME, jsonMessage);
      record.headers().add("type", m.getType().getBytes());

      // and send it
      kafkaTemplate.send(record);
    } catch (Exception e) {
      throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
    }
  }
}
