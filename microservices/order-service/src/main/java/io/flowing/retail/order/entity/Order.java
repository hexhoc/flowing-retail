package io.flowing.retail.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("orderId")
    private UUID id;

    private UUID customerId;

    // TODO Обновлять статус заказа в зависимости от этапов его обработки
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus = OrderStatusEnum.NEW;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<OrderItem>();

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Version
    private Integer version;

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
        items.add(i);
    }

    public int getTotalSum() {
        int sum = 0;
        for (OrderItem orderItem : items) {
            sum += orderItem.getAmount();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", items=" + items + "]";
    }


}
