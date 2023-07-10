package io.flowing.retail.order.controller.order;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.flowing.retail.order.dto.OrderCommandDTO;
import io.flowing.retail.order.dto.OrderQueryDTO;
import io.flowing.retail.order.service.OrderQueryService;
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
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    @Operation(summary = "Test method", description = "Check is everything ok, and service already started")
    @GetMapping("/test")
    public ResponseEntity<String> testGet() {
        return ResponseEntity.ok("HELLO");
    }

    @Operation(summary = "Get a list of orders", description = "Check is everything ok, and service already started")
    @GetMapping
    public ResponseEntity<Page<OrderQueryDTO>> orderGetList(
            @Parameter(description = "customer id filter")
            @RequestParam(name = "customerId", required = false) Integer customerId,
            @Parameter(description = "Page, default is 0")
            @RequestParam(name = "Page", required = false, defaultValue = "0") Integer page,
            @Parameter(description = "limit, default is 10")
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) {

        log.info("get order list");
        return ResponseEntity.ok(orderQueryService.findAll(customerId, page, limit));
    }

    @Operation(summary = "Find order by ID", description = "Return single order")
    @GetMapping(path = "/{id}")
    public ResponseEntity<OrderQueryDTO> orderGetById(@Valid @NotNull @PathVariable(name = "id") String id) {
        log.info("get order by id");
        return ResponseEntity.ok(orderQueryService.findById(id));
    }

}