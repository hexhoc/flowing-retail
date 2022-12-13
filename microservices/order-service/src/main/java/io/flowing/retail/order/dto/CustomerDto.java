package io.flowing.retail.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Customer information")
public class CustomerDto {
  @Schema(description = "Customer id")
  @NotBlank
  @JsonProperty("id")
  private UUID id = null;

  @Schema(description = "Customer name")
  private String name;
  @Schema(description = "Customer name", example = "Germany, sesame street, 21")
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
