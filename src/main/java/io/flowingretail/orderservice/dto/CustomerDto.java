package io.flowingretail.orderservice.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
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
