package io.flowing.retail.order.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="order_items")
@Getter
@Setter
public class OrderItem {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Type(type="org.hibernate.type.PostgresUUIDType")
  @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()", updatable = false, nullable = false)
  private UUID id;


  @NotNull
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @NotNull
  @Column(name = "product_id", nullable = false)
  private Integer productId;

  @NotNull
  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @NotNull
  @Column(name = "price", nullable = false)
  private BigDecimal price;

  @Override
  public String toString() {
    return "OrderItem{" +
            "id=" + id +
            ", productId=" + productId +
            ", quantity=" + quantity +
            ", price=" + price +
            '}';
  }
}
