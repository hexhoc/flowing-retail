package io.flowing.retail.paymentservice.repository;

import io.flowing.retail.paymentservice.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Page<Payment> findAllByOrderId(Integer orderId, Pageable pageable);

    Page<Payment> findAllByCustomerId(Integer customerId, Pageable pageable);

    Page<Payment> findAllByOrderIdAndCustomerId(Integer orderId, Integer customerId, Pageable pageable);
}