package io.flowing.retail.customerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "Order data transfer object. Used for request and response")
public class CustomerDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Integer id;
    
    @Schema(description = "first name")
    private String firstName;

    @Schema(description = "last name")
    private String lastName;

    @Schema(description = "email")
    private String email;

    @Schema(description = "phone")
    private String phone;

    @Schema(description = "is deleted")
    private Boolean isDeleted = false;

    @Schema(description = "created date")
    private LocalDateTime createdDate;

    @Schema(description = "modified date")
    private LocalDateTime modifiedDate;

    @Schema(description = "version")
    private Long version;
}
