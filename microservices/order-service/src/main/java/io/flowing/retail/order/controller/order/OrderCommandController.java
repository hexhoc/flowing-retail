package io.flowing.retail.order.controller.order;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.flowing.retail.order.dto.OrderCommandDTO;
import io.flowing.retail.order.service.OrderCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@Log4j2
@ConditionalOnProperty(name = "app.write.enabled", havingValue = "false")
public class OrderCommandController {

    private final OrderCommandService orderCommandService;

    @Operation(summary = "Test method", description = "Check is everything ok, and service already started")
    @GetMapping("/test")
    public ResponseEntity<String> testGet() {
        return ResponseEntity.ok("HELLO");
    }

    @Operation(summary = "Add new order", description = "Return created order")
    @PostMapping
    public ResponseEntity<OrderCommandDTO> orderPost(
            @Parameter(description = "Order to create. Cannot null or empty.", required = true)
            @RequestBody OrderCommandDTO orderCommandDto) {
        log.info("Add new order");
        return ResponseEntity.ok(orderCommandService.createOrder(orderCommandDto));
    }

    @Operation(summary = "Update an existing order", description = "Return updated order")
    @PutMapping("/{id}")
    public ResponseEntity<OrderCommandDTO> orderPut(
            @Parameter(description = "Order id. Cannot null or empty.", required = true)
            @Valid @NotNull @PathVariable("id") String id,
            @Parameter(description = "Order to update. Cannot null or empty.", required = true)
            @RequestBody OrderCommandDTO orderCommandDto) {
        log.info("update order");
        return ResponseEntity.ok(orderCommandService.updateOrder(id, orderCommandDto));
    }

    @Operation(summary = "Delete order by ID", description = "Return \"OK\"")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> orderDeleteById(
            @Parameter(description = "Id of the order to be delete. Cannot be empty.", required = true)
            @PathVariable(name = "id") String id) {
        orderCommandService.deleteById(id);
        log.info("delete order");
        return ResponseEntity.ok("OK");
    }

}