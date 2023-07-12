package io.flowing.retail.order.repository;

import java.util.UUID;

import io.flowing.retail.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Write-only order repository
 */
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findAllByCustomerId(Integer customerId, Pageable pageable);
}
