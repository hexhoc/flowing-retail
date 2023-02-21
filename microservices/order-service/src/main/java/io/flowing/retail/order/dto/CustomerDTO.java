package io.flowing.retail.order.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean isDeleted = false;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long version;
}
