package io.flowingretail.productservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
@Schema(description = "Product image DTO")
public class ProductImageDto implements Serializable {
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
