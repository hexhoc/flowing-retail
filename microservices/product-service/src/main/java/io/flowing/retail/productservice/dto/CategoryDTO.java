package io.flowing.retail.productservice.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CategoryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer level;

    private Long parentId;

    private String name;

    private Long rank;

    private Boolean isDeleted;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Long version;

}
