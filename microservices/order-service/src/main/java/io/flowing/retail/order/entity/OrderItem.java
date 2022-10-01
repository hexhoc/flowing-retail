package io.flowing.retail.order.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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

  @ManyToOne
  private Order order;

  private String articleId;

  private int amount;


  @Override
  public String toString() {
    return "OrderItem [articleId=" + articleId + ", amount=" + amount + "]";
  }
}
