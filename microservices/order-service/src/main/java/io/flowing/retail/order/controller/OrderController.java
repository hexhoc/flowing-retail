package io.flowing.retail.order.controller;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.dto.OrderDto;
import io.flowing.retail.order.mapper.OrderMapper;
import io.flowing.retail.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {

  private final OrderService  orderService;
  private final OrderMapper orderMapper;

  @RolesAllowed({ "ADMIN", "USER" })
  @GetMapping("/api/test/all")
  public ResponseEntity<String> testAllGet() {
    return ResponseEntity.ok("HELLO EVERYONE");
  }

  @RolesAllowed("ADMIN")
  @GetMapping("/api/test/admin")
  public ResponseEntity<String> testAdminGet() {
    return ResponseEntity.ok("HELLO ADMIN");
  }

  @PostMapping(path = "/api/cart/order")
  public ResponseEntity<Order> OrderPost(@RequestBody OrderDto orderDto) {
    Order order = orderService.createOrder(orderMapper.toModel(orderDto));
    return ResponseEntity.ok(order);
  }

}