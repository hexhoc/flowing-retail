package io.flowing.retail.productservice.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class ReviewDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer productId;

    private Integer customerId;

    private Short rating;

    private String content;

    private Boolean isDeleted = false;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    private Long version;

}
