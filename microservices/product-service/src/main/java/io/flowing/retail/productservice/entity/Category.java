package io.flowing.retail.productservice.entity;

//import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rank", nullable = false)
    private Long rank;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "created_date", nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "version")
    @Version
    private Long version;

}
