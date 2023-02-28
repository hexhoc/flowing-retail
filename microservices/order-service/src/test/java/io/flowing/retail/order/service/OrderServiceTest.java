package io.flowing.retail.order.service;

import io.flowing.retail.order.dto.OrderDTO;
import io.flowing.retail.order.dto.mapper.OrderMapper;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.entity.OrderItem;
import io.flowing.retail.order.entity.enums.OrderStatusEnum;
import io.flowing.retail.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order mockOrder;

    @BeforeEach
    void setUp() {
        mockOrder = new Order();
        mockOrder.setId(UUID.randomUUID());
        mockOrder.setCustomerId(1);
        mockOrder.setAddress("Germany");
        mockOrder.setStatus(OrderStatusEnum.NEW);
        mockOrder.setIsDeleted(false);
        mockOrder.setOrderItems(
                Set.of(
                        new OrderItem(UUID.randomUUID(), mockOrder, 1, 1, BigDecimal.valueOf(100)),
                        new OrderItem(UUID.randomUUID(), mockOrder, 2, 1, BigDecimal.valueOf(200))
                ));
    }

    @DisplayName("test for create order")
    @Test
    void createOrder() {
    }

    @DisplayName("test for update order")
    @Test
    void givenOrderObject_whenUpdateOrder_thenReturnUpdatedOrder() {
        // given - precondition or setup
        given(orderRepository.findById(UUID.fromString(mockOrder.getId()))).willReturn(Optional.of(mockOrder));
        given(orderRepository.save(mockOrder)).willReturn(mockOrder);
        mockOrder.setCustomerId(2);
        mockOrder.setAddress("USA");
        // when -  action or the behaviour that we are going test
        OrderDTO updatedOrder = orderService.updateOrder(mockOrder.getId(), OrderMapper.toDto(mockOrder));
        // then - verify the output
        assertThat(updatedOrder.getCustomerId()).isEqualTo(2);
        assertThat(updatedOrder.getAddress()).isEqualTo("USA");
    }

    @DisplayName("test for find all order")
    @Test
    void givenOrderList_whenGetAllOrder_thenReturnOrderList() {
        // given - precondition or setup
        Order mockOrder2 = new Order();
        BeanUtils.copyProperties(mockOrder, mockOrder2);
        mockOrder2.setId(UUID.randomUUID());

        given(orderRepository.findAll(PageRequest.of(0,2)))
                .willReturn(new PageImpl<>(List.of(mockOrder,mockOrder2)));

        // when -  action or the behaviour that we are going test
        Page<OrderDTO> orderList = orderService.findAll(null,0,2);

        // then - verify the output
        assertThat(orderList).isNotNull();
        assertThat(orderList.getTotalElements()).isEqualTo(2);
    }

    @DisplayName("test for find all order (negative scenario)")
    @Test
    void givenEmptyOrderist_whenGetAllOrder_thenReturnEmptyOrderList() {
        // given - precondition or setup
        given(orderRepository.findAll(PageRequest.of(0,2)))
                .willReturn(new PageImpl<>(Collections.emptyList()));

        // when -  action or the behaviour that we are going test
        Page<OrderDTO> orderList = orderService.findAll(null,0,2);

        // then - verify the output
        assertThat(orderList).isEmpty();
    }


    @DisplayName("test for find by id order")
    @Test
    void findById() {
        // given
        given(orderRepository.findById(UUID.fromString(mockOrder.getId()))).willReturn(Optional.of(mockOrder));
        // when
        OrderDTO orderDto = orderService.findById(mockOrder.getId());
        // then
        assertThat(orderDto).isNotNull();
    }

    @DisplayName("test for update status order")
    @Test
    void updateStatus() {
        // given
        given(orderRepository.save(mockOrder)).willReturn(mockOrder);
        // when
        orderService.updateStatus(mockOrder.getId(), OrderStatusEnum.SHIPMENT_READY);
        // then
        assertThat(mockOrder.getStatus()).isEqualTo(OrderStatusEnum.SHIPMENT_READY);
    }
}