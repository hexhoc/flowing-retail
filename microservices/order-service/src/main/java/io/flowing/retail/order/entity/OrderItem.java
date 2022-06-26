package io.flowing.retail.order.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Table(name="order_items")
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

  /////////////////////////
  // GETTER AND SETTER
  /////////////////////////
  public String getArticleId() {
    return articleId;
  }
  public OrderItem setArticleId(String articleId) {
    this.articleId = articleId;
    return this;
  }
  public int getAmount() {
    return amount;
  }
  public OrderItem setAmount(int amount) {
    this.amount = amount;
    return this;
  }
  @Override
  public String toString() {
    return "OrderItem [articleId=" + articleId + ", amount=" + amount + "]";
  }
}
