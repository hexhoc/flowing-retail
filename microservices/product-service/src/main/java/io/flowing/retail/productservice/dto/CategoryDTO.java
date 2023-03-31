package io.flowing.retail.productservice.dto;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CategoryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Short level;

    private Integer parentId;

    private String name;

    private Integer rank;

    private Boolean isDeleted;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    private Long version;

}
