package io.flowing.retail.productservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "Product image DTO")
public class ProductImageDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Product data transfer object. Used for request and response")
    private Integer id;

    @Schema(description = "image name (or full path)")
    @NotNull
    private String name;

    @Schema(description = "Product id")
    @NotNull
    private Integer ProductId;

}
