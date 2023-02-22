package io.flowing.retail.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.flowing.retail.order.entity.enums.OrderStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Size(max = 20)
    @NotNull
    @Column(name = "order_no", nullable = false, length = 20)
    private String orderNo;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

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

    @NotNull
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    /////////////////////////
    // GETTER AND SETTER
    /////////////////////////
    public boolean isNew() {
        return this.id == null;
    }

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        if (Objects.nonNull(id)) {
            this.id = UUID.fromString(id);
        }
    }

    public void addItem(OrderItem i) {
        orderItems.add(i);
    }

    public BigDecimal getTotalSum() {
        //TODO: Fix сделать нормальный расчет суммы с использование только bigdecimal, без double
        double sum = 0D;
        for (OrderItem orderItem : orderItems) {
            sum += orderItem.getPrice().doubleValue() * orderItem.getQuantity();
        }
        return new BigDecimal(sum);
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", items=" + orderItems + "]";
    }

}
