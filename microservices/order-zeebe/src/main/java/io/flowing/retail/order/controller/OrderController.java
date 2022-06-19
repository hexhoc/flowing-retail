package io.flowing.retail.order.controller;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.dto.OrderDto;
import io.flowing.retail.order.mapper.OrderMapper;
import io.flowing.retail.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin

public class OrderController {

  private final OrderService  orderService;
  private final OrderMapper orderMapper;

  @GetMapping("/api/test")
  public ResponseEntity<String> testGet() {
    return ResponseEntity.ok("HELLO");
  }
  @PostMapping(path = "/api/cart/order")
  public ResponseEntity<Order> OrderPost(@RequestBody OrderDto orderDto) {
    Order order = orderService.createOrder(orderMapper.toModel(orderDto));
    return ResponseEntity.ok(order);
  }

}