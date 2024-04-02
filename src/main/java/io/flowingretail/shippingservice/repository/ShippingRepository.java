package io.flowingretail.shippingservice.repository;

import io.flowingretail.shippingservice.entity.Waybill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<Waybill, Integer> {
    Page<Waybill> findAllByOrderId(Integer orderId, Pageable pageable);
}