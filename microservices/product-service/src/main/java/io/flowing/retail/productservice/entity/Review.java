package io.flowing.retail.productservice.entity;

//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Short rating;

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @NotNull
    @Column(name = "created_date", nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "version")
    @Version
    private Long version;
}