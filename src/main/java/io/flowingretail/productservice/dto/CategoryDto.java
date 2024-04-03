package io.flowingretail.productservice.dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class CategoryDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Short level;

    private Integer parentId;

    private String name;

    private Integer rank;

    private Boolean isDeleted = false;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    private Long version;

}
