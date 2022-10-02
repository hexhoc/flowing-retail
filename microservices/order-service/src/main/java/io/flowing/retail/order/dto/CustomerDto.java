package io.flowing.retail.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class CustomerDto {
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
