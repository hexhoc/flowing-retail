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
@RequestMapping("/api/v1/order")
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/test")
  public ResponseEntity<String> testGet() {
    return ResponseEntity.ok("HELLO");
  }

  @GetMapping
  public ResponseEntity<Page<OrderDto>> OrderGetList(@RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                        @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {
    var pageRequest = PageRequest.of(offset/limit, limit);
    return ResponseEntity.ok(orderService.findAll(pageRequest));
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<OrderDto> orderGetById(@PathVariable(name = "id") String id) {
    return ResponseEntity.ok(orderService.findById(id));
  }

  @PostMapping
  public ResponseEntity<OrderDto> orderPost(@RequestBody OrderDto orderDto) {
    return ResponseEntity.ok(orderService.createOrder(orderDto));
  }

  @PutMapping
  public ResponseEntity<OrderDto> orderPut(@RequestBody OrderDto orderDto) {
    return ResponseEntity.ok(orderService.updateOrder(orderDto));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<String> orderDeleteById(@PathVariable(name = "id") String id) {
    orderService.deleteById(id);
    return ResponseEntity.ok("OK");
  }

}