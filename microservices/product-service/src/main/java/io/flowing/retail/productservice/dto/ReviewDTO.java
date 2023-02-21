package io.flowing.retail.productservice.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ReviewDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer productId;

    private Integer customerId;

    private Short rating;

    private String content;

    private Boolean isDeleted;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Long version;

}
