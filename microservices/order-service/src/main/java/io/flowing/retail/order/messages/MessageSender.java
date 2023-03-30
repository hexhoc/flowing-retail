package io.flowing.retail.order.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Helper to send messages, currently nailed to Kafka, but could also send via AMQP (e.g. RabbitMQ) or
 * any other transport easily
 */
@Component
@RequiredArgsConstructor
@Log4j2
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
      ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);

      future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
        @Override
        public void onSuccess(SendResult<String, String> result) {
          log.info("Message [{}] delivered with offset {}",
                  record,
                  result.getRecordMetadata().offset());
        }

        @Override
        public void onFailure(Throwable ex) {
          log.warn("Unable to deliver message [{}]. {}",
                  record,
                  ex.getMessage());
        }
      });
    } catch (Exception e) {
      throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
    }
  }
}
