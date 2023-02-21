package io.flowing.retail.productservice.entity;

//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product_images")
@Getter
@Setter
public class ProductImage {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "product_id", nullable = false)
    @NotNull
    private Integer productId;

    @Lob
    @Column(name = "image_bytes", columnDefinition = "bytea")
    private byte[] imageBytes;
}