package io.flowingretail.productservice.dto;


import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
public class TagDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String description;

}
