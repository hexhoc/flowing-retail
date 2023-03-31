package io.flowing.retail.productservice.entity;

//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 300)
    @NotNull
    @Column(name = "name", nullable = false, length = 300)
    private String name;

    @Size(max = 500)
    @NotNull
    @Column(name = "intro", nullable = false, length = 500)
    private String intro;

    @Size(max = 500)
    @NotNull
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @NotNull
    @Column(name = "original_price", nullable = false)
    private BigDecimal originalPrice;

    @NotNull
    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;

    @NotNull
    @Column(name = "is_sale", nullable = false)
    private Boolean isSale = false;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "modified_date", nullable = false)
    @UpdateTimestamp
    private Timestamp modifiedDate;

    @Column(name = "version")
    @Version
    private Long version;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Review> reviews = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_tag",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new LinkedHashSet<>();

    @OneToMany
    @JoinColumn(name="product_id")
    private Set<ProductImage> productImages = new LinkedHashSet<>();

}