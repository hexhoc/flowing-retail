package io.flowing.retail.order.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="customers")
@Getter
@Setter
public class Customer {
  
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Type(type="org.hibernate.type.PostgresUUIDType")
  @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()", updatable = false, nullable = false)
  private UUID id;

  private String name;

  private String address;

  @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
  @CreationTimestamp
  private LocalDateTime createdDate;

  @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
  @UpdateTimestamp
  private LocalDateTime updatedDate;

  @Version
  private Integer version;

  public Customer() {
  }

}
