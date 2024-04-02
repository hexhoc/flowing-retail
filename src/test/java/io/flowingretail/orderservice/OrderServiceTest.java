package io.flowingretail.orderservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import io.flowingretail.orderservice.dto.mapper.OrderItemMapper;
import io.flowingretail.orderservice.dto.mapper.OrderMapper;
import io.flowingretail.orderservice.entity.Order;
import io.flowingretail.orderservice.entity.OrderItem;
import io.flowingretail.orderservice.entity.enums.OrderStatusEnum;
import io.flowingretail.orderservice.repository.OrderRepository;
import io.flowingretail.orderservice.service.OrderService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Spy
    private final OrderMapper orderMapper = new OrderMapper(new OrderItemMapper());

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
        given(orderRepository.save(any())).willReturn(mockOrder);
        mockOrder.setCustomerId(2);
        mockOrder.setAddress("USA");
        // when -  action or the behaviour that we are going test
        var updatedOrder = orderService.updateOrder(mockOrder.getId(), orderMapper.toDTO(mockOrder));
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

        given(orderRepository.findAll(PageRequest.of(0, 2)))
                .willReturn(new PageImpl<>(List.of(mockOrder,mockOrder2)));

        // when -  action or the behaviour that we are going test
        var orderList = orderService.findAll(null, 0, 2);

        // then - verify the output
        assertThat(orderList).isNotNull();
        assertThat(orderList.getTotalElements()).isEqualTo(2);
    }

    @DisplayName("test for find all order (negative scenario)")
    @Test
    void givenEmptyOrderist_whenGetAllOrder_thenReturnEmptyOrderList() {
        // given - precondition or setup
        given(orderRepository.findAll(PageRequest.of(0, 2)))
                .willReturn(new PageImpl<>(Collections.emptyList()));

        // when -  action or the behaviour that we are going test
        var orderList = orderService.findAll(null, 0, 2);

        // then - verify the output
        assertThat(orderList).isEmpty();
    }


    @DisplayName("test for find by id order")
    @Test
    void findById() {
        // given
        given(orderRepository.findById(UUID.fromString(mockOrder.getId()))).willReturn(Optional.of(mockOrder));
        // when
        var orderDto = orderService.findById(mockOrder.getId());
        // then
        assertThat(orderDto).isNotNull();
    }

    @DisplayName("test for update status order")
    @Test
    void updateStatus() {
        // given
        given(orderRepository.findById(UUID.fromString(mockOrder.getId()))).willReturn(Optional.of(mockOrder));
        given(orderRepository.save(mockOrder)).willReturn(mockOrder);
        // when
        orderService.updateStatus(mockOrder.getId(), OrderStatusEnum.PICKED_UP);
        // then
        assertThat(mockOrder.getStatus()).isEqualTo(OrderStatusEnum.PICKED_UP);
    }
}