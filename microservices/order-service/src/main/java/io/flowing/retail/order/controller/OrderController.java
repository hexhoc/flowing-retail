package io.flowing.retail.order.controller;

import io.flowing.retail.order.dto.OrderDTO;
import io.flowing.retail.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/order")
@Tag(name = "order", description = "the order API")
@Log4j2
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Test method", description = "Check is everything ok, and service already started")
    @GetMapping("/test")
    public ResponseEntity<String> testGet() {
        return ResponseEntity.ok("HELLO");
    }

    @Operation(summary = "Get a list of orders", description = "Check is everything ok, and service already started")
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> orderGetList(
            @Parameter(description="customer id filter")
            @RequestParam(name = "customerId", required = false) Integer customerId,
            @Parameter(description="Page, default is 0")
            @RequestParam(name = "Page", required = false, defaultValue = "0") Integer page,
            @Parameter(description="limit, default is 10")
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {

        log.info("get order list");
        return ResponseEntity.ok(orderService.findAll(customerId, page, limit));
    }

    @Operation(summary = "Find order by ID", description = "Return single order")
    @GetMapping(path = "/{id}")
    public ResponseEntity<OrderDTO> orderGetById(@Valid @NotNull @PathVariable(name = "id") String id) {
        log.info("get order by id");
        return ResponseEntity.ok(orderService.findById(id));
    }

    @Operation(summary = "Add new order", description = "Return created order")
    @PostMapping
    public ResponseEntity<OrderDTO> orderPost(
            @Parameter(description="Order to create. Cannot null or empty.", required=true)
            @RequestBody OrderDTO orderDto) {
        log.info("Add new order");
        return ResponseEntity.ok(orderService.createOrder(orderDto));
    }

    @Operation(summary = "Update an existing order", description = "Return updated order")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> orderPut(
            @Parameter(description="Order id. Cannot null or empty.", required=true)
            @Valid @NotNull @PathVariable("id") String id,
            @Parameter(description="Order to update. Cannot null or empty.", required=true)
            @RequestBody OrderDTO orderDto) {
        log.info("update order");
        return ResponseEntity.ok(orderService.updateOrder(id, orderDto));
    }

    @Operation(summary = "Delete order by ID", description = "Return \"OK\"")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> orderDeleteById(
            @Parameter(description="Id of the order to be delete. Cannot be empty.", required=true)
            @PathVariable(name = "id") String id) {
        orderService.deleteById(id);
        log.info("delete order");
        return ResponseEntity.ok("OK");
    }

}