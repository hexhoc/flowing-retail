package io.flowing.retail.productservice.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TagDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String description;

}
