package io.flowing.retail.order.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import io.flowing.retail.order.dto.OrderCommandDTO;
import io.flowing.retail.order.dto.OrderQueryDTO;
import io.flowing.retail.order.dto.mapper.OrderMapper;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.repository.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;

    private final OrderMapper orderMapper;

    public Page<OrderQueryDTO> findAll(Integer customerId, Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);

        Page<Order> entityPage;
        if (Objects.nonNull(customerId)) {
            entityPage = orderQueryRepository.findAllByCustomerId(customerId, pageRequest);
        } else {
            entityPage = orderQueryRepository.findAll(pageRequest);
        }

        List<OrderQueryDTO> dtoList = entityPage.stream()
                                                  .map(orderMapper::toOrderQueryDTO)
                                                  .toList();

        return new PageImpl<>(dtoList);
    }

    public OrderQueryDTO findById(String id) {
        return orderMapper.toOrderQueryDTO(requireOne(id));
    }

    private Order requireOne(String id) {
        return orderQueryRepository.findById(UUID.fromString(id))
                                   .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

}
