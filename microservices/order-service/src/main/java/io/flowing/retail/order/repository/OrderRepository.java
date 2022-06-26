package io.flowing.retail.order.repository;

import io.flowing.retail.order.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, String> {

}
