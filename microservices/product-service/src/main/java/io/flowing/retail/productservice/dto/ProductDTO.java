package io.flowing.retail.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Schema(description = "Product data transfer object. Used for request and response")
public class ProductDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Product id")
    @Null
    private Integer id;

    @Schema(description = "Product name")
    @NotNull
    @Size(min = 5, message = "Product name should have at least 5 characters")
    private String name;

    @Schema(description = "Product barcode")
    private String sku;

    @Schema(description = "Product intro (short description)")
    @NotNull
    @NotBlank(message = "Content cannot be empty")
    private String intro;

    @Schema(description = "Product description")
    @NotNull
    @NotBlank(message = "Content cannot be empty")
    private String description;

    @Schema(description = "Product category")
    @NotNull
    private CategoryDTO category;

    @NotNull
    @Schema(description = "Product original price")
    @NotNull
    @Min(value = 100, message = "The original price must be at least 100")
    private BigDecimal originalPrice;

    @Schema(description = "Product selling price")
    @NotNull
    @Min(value = 100, message = "The sale price must be at least 100")
    private BigDecimal sellingPrice;

    @Schema(description = "Product is sale")
    @NotNull
    private Boolean isSale;

    @Schema(description = "Product tags")
    private Set<TagDTO> tags;

    @Schema(description = "Product images")
    private Set<ProductImageDTO> images;

    @Schema(description = "Product is deleted")
    @NotNull
    private Boolean isDeleted;

    @Schema(description = "Product created date")
    @Null
    private Timestamp createdDate;

    @Schema(description = "Product last modified date")
    @Null
    private Timestamp modifiedDate;

    @Schema(description = "Product version")
    @Null
    private Long version;

}
