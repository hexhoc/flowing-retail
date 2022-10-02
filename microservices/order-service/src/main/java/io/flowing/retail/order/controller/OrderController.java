package io.flowing.retail.order.controller;

import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.dto.OrderDto;
import io.flowing.retail.order.mapper.OrderMapper;
import io.flowing.retail.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1")
public class OrderController {

  private final OrderService  orderService;
  private final OrderMapper orderMapper;

  @GetMapping("/test")
  public ResponseEntity<String> testGet() {
    return ResponseEntity.ok("HELLO");
  }

  @GetMapping(path = "/order")
  public ResponseEntity<Page<Order>> OrderGetList(@RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                        @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {
    var pageRequest = PageRequest.of(offset/limit, limit);
    Page<Order> orders = orderService.findAll(pageRequest);
    return ResponseEntity.ok(orders);
  }

  @GetMapping(path = "/order/{id}")
  public ResponseEntity<OrderDto> OrderGetById(@PathVariable(name = "id") String id) {
    Order order = orderService.findById(id);
    return ResponseEntity.ok(orderMapper.toDto(order));
  }

  @PostMapping(path = "/order")
  public ResponseEntity<Order> OrderPost(@RequestBody OrderDto orderDto) {
    Order order = orderService.createOrder(orderMapper.toModel(orderDto));
    return ResponseEntity.ok(order);
  }

  @PutMapping(path = "/order")
  public ResponseEntity<OrderDto> OrderPut(@RequestBody OrderDto orderDto) {
    Order order = orderService.updateOrder(orderMapper.toModel(orderDto));
    return ResponseEntity.ok(orderMapper.toDto(order));
  }

  @DeleteMapping(path = "/order/{id}")
  public ResponseEntity<String> OrderDeleteById(@PathVariable(name = "id") String id) {
    orderService.deleteById(id);
    return ResponseEntity.ok("OK");
  }

}