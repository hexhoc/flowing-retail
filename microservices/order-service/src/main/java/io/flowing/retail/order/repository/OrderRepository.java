package io.flowing.retail.order.repository;

import io.flowing.retail.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
