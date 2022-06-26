package io.flowing.retail.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Type(type="org.hibernate.type.PostgresUUIDType")
  @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER  )
  private Customer customer = new Customer();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER  )
  private List<OrderItem> items = new ArrayList<OrderItem>();

  @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
  @CreationTimestamp
  private LocalDateTime createdDate;

  @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
  @UpdateTimestamp
  private LocalDateTime updatedDate;

  @Version
  private Integer version;

  /////////////////////////
  // GETTER AND SETTER
  /////////////////////////
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
  
  public String getId() {
    return id.toString();
  }

  @JsonProperty("orderId")
  public void setId(String id) {
    this.id = UUID.fromString(id);
  }

  public List<OrderItem> getItems() {
    return items;
  }

  @Override
  public String toString() {
    return "Order [id=" + id + ", items=" + items + "]";
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }


  
}
