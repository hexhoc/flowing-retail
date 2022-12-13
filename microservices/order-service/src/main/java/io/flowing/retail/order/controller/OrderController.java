package io.flowing.retail.order.controller;

import io.flowing.retail.order.dto.OrderDto;
import io.flowing.retail.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/order")
@Tag(name = "order", description = "the order API")
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "Test method", description = "Check is everything ok, and service already started")
  @GetMapping("/test")
  public ResponseEntity<String> testGet() {
    return ResponseEntity.ok("HELLO");
  }

  @Operation(summary = "Get a list of orders", description = "Check is everything ok, and service already started")
  @GetMapping
  public ResponseEntity<Page<OrderDto>> orderGetList(
          @Parameter(description="Page limit, default is 10")
          @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
          @Parameter(description="Page offset, default is 0")
          @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {
    var pageRequest = PageRequest.of(offset/limit, limit);
    return ResponseEntity.ok(orderService.findAll(pageRequest));
  }

  @Operation(summary = "Find order by ID", description = "Return single order")
  @GetMapping(path = "/{id}")
  public ResponseEntity<OrderDto> orderGetById(@PathVariable(name = "id") String id) {
    return ResponseEntity.ok(orderService.findById(id));
  }

  @Operation(summary = "Add new order", description = "Return created order")
  @PostMapping
  public ResponseEntity<OrderDto> orderPost(
          @Parameter(description="Order to create. Cannot null or empty.", required=true)
          @RequestBody OrderDto orderDto) {
    return ResponseEntity.ok(orderService.createOrder(orderDto));
  }

  @Operation(summary = "Update an existing order", description = "Return updated order")
  @PutMapping
  public ResponseEntity<OrderDto> orderPut(
          @Parameter(description="Order to update. Cannot null or empty.", required=true)
          @RequestBody OrderDto orderDto) {
    return ResponseEntity.ok(orderService.updateOrder(orderDto));
  }

  @Operation(summary = "Delete order by ID", description = "Return \"OK\"")
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<String> orderDeleteById(
          @Parameter(description="Id of the order to be delete. Cannot be empty.", required=true)
          @PathVariable(name = "id") String id) {
    orderService.deleteById(id);
    return ResponseEntity.ok("OK");
  }

}