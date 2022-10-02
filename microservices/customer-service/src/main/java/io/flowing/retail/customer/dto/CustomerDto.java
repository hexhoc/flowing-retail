package io.flowing.retail.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerDto {

  @JsonProperty("id")
  private UUID id = null;

  //TODO add springdoc
  private String name;
  private String address;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer version;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
  private LocalDateTime createdDate;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
  private LocalDateTime updatedDate;

}
