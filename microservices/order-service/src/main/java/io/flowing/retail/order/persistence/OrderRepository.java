package io.flowing.retail.order.persistence;

import io.flowing.retail.order.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, String> {

}
