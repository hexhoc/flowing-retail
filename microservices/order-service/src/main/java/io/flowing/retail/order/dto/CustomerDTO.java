package io.flowing.retail.order.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class CustomerDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean isDeleted = false;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private Long version;
}
