package io.flowing.retail.paymentservice.service;

import io.flowing.retail.paymentservice.dto.PaymentDTO;
import io.flowing.retail.paymentservice.dto.mapper.PaymentMapper;
import io.flowing.retail.paymentservice.entity.Payment;
import io.flowing.retail.paymentservice.enums.PaymentType;
import io.flowing.retail.paymentservice.messages.payload.RetrievePaymentCommandPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import io.flowing.retail.paymentservice.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /**
     * Receive payment from customer
     *
     * @param retrievePaymentCommand info about order
     * @throws InterruptedException
     */
    public String receive(RetrievePaymentCommandPayload retrievePaymentCommand) throws InterruptedException {
        log.info(String.format("Payment has been received. Order id: %s, reason: %s, amount: %s",
                retrievePaymentCommand.getRefId(),
                retrievePaymentCommand.getReason(),
                retrievePaymentCommand.getAmount()));

        Payment p = new Payment();
        p.setDate(LocalDateTime.now());
        p.setOrderId(retrievePaymentCommand.getRefId());
        p.setCustomerId(retrievePaymentCommand.getCustomerId());
        p.setType(PaymentType.INCOMING);
        p.setAmount(BigDecimal.valueOf(retrievePaymentCommand.getAmount()));

        paymentRepository.save(p);

        Thread.sleep(60_000);

        return p.getId().toString();
    }

    public void delete(Integer id) {
        paymentRepository.deleteById(id);
    }

    public void update(Integer id, PaymentDTO dto) {
        Payment entity = requireOne(id);
        Payment updatedEntity = PaymentMapper.toEntity(dto);
        BeanUtils.copyProperties(updatedEntity, entity, "id","version","createdDate","modifiedDate");
        paymentRepository.save(entity);
    }

    public Page<PaymentDTO> getAll(Integer orderId, Integer customerId, Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);

        // TODO: Use JpaRepository instead
        Page<Payment> entityPage;
        if (Objects.nonNull(orderId) && Objects.nonNull(customerId)){
            entityPage = paymentRepository.findAllByOrderIdAndCustomerId(orderId, customerId, pageRequest);
        } else if (Objects.nonNull(orderId)) {
            entityPage = paymentRepository.findAllByOrderId(orderId, pageRequest);
        } else if (Objects.nonNull(customerId)) {
            entityPage = paymentRepository.findAllByCustomerId(customerId, pageRequest);
        } else {
            entityPage = paymentRepository.findAll(pageRequest);
        }

        List<PaymentDTO> dtoList = entityPage.stream()
                .map(PaymentMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

    public PaymentDTO getById(Integer id) {
        return PaymentMapper.toDto(requireOne(id));
    }

    private Payment requireOne(Integer id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
