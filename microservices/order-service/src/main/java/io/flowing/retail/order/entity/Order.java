package io.flowing.retail.order.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import io.flowing.retail.order.entity.enums.OrderStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date", nullable = false)
    private Timestamp modifiedDate;

    @Version
    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    /////////////////////////
    // GETTER AND SETTER
    /////////////////////////
    public boolean isNew() {
        return this.id == null;
    }

    public String getId() {
        if (Objects.isNull(id)) {
            return "";
        }
        return id.toString();
    }

    public void setId(String id) {
        if (Objects.nonNull(id)) {
            this.id = UUID.fromString(id);
        }
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void addItem(OrderItem i) {
        orderItems.add(i);
    }

    public BigDecimal getTotalSum() {
        BigDecimal totalSum = BigDecimal.ONE;
        for (OrderItem orderItem : orderItems) {
            var itemSum = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            totalSum = totalSum.add(itemSum);
        }
        return totalSum;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", items=" + orderItems + "]";
    }

}
