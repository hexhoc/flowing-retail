package io.flowing.retail.checkout.messages;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Message<T> {

  // Cloud Events attributes (https://github.com/cloudevents/spec/blob/v1.0/spec.md)
  private String type;
  private String id = UUID.randomUUID().toString(); // unique id of this message
  private String source = "Checkout";
  @JsonFormat(shape = JsonFormat.Shape.STRING) // ISO-8601 compliant format  
  private Instant time = Instant.now();
  private T data;
  private String datacontenttype="application/json";
  private String specversion="1.0";
  
  // Extension attributes
  private String traceid = UUID.randomUUID().toString(); // trace id, default: new unique
  private String correlationid; // id which can be used for correlation later if required
  private String group = "flowing-retail";

  public Message(String type, T payload) {
    this.type = type;
    this.data = payload;
  }
  
  public Message(String type, String traceid, T payload) {
    this.type = type;
    this.traceid = traceid;
    this.data = payload;
  }

}
