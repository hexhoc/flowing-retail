package io.flowingretail.orderservice.controller;

import io.flowingretail.orderservice.dto.OrderDTO;
import io.flowingretail.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/order")
@Tag(name = "order", description = "the order API")
@Slf4j
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